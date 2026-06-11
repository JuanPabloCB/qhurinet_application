package pe.edu.upc.qhurinet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.HistorialMaterialDTO;
import pe.edu.upc.qhurinet.dtos.PublicacionBusquedaDTO;
import pe.edu.upc.qhurinet.dtos.PublicacionCategoriaMaterialDTO;
import pe.edu.upc.qhurinet.dtos.PublicacionDetalleDTO;
import pe.edu.upc.qhurinet.dtos.PublicacionDTO;
import pe.edu.upc.qhurinet.dtos.PublicacionDistanciaDTO;
import pe.edu.upc.qhurinet.dtos.PublicacionMaterialDetalleDTO;
import pe.edu.upc.qhurinet.entities.Calificacion;
import pe.edu.upc.qhurinet.entities.Publicacion;
import pe.edu.upc.qhurinet.entities.PublicacionMaterial;
import pe.edu.upc.qhurinet.entities.Recoleccion;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.ICalificacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IPublicacionMaterialService;
import pe.edu.upc.qhurinet.servicesinterfaces.IPublicacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IRecoleccionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
public class PublicacionController {
    @Autowired
    private IPublicacionService pS;

    @Autowired
    private IUsuarioService uS;

    @Autowired
    private IPublicacionMaterialService pMS;

    @Autowired
    private IRecoleccionService rS;

    @Autowired
    private ICalificacionService cS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    public List<PublicacionDTO> list() {
        return pS.list().stream().map(this::toDTO).toList();
    }

    @GetMapping("/activos")
    public List<PublicacionDTO> publicacionesActivas() {
        return pS.list().stream()
                .filter(publicacion -> Boolean.TRUE.equals(publicacion.getActivo()))
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public PublicacionDTO listId(@PathVariable Long id) {
        return toDTO(pS.listId(id).orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada")));
    }

    @GetMapping("/{id}/detalle")
    public PublicacionDetalleDTO detalle(@PathVariable Long id) {
        Publicacion publicacion = pS.listId(id).orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));
        List<PublicacionMaterialDetalleDTO> materiales = pMS.materialesPorPublicacion(id).stream()
                .map(this::toMaterialDetalleDTO)
                .toList();
        List<Calificacion> calificaciones = rS.list().stream()
                .filter(recoleccion -> recoleccion.getPublicacion() != null && id.equals(recoleccion.getPublicacion().getId()))
                .flatMap(recoleccion -> cS.calificacionesPorRecoleccion(recoleccion.getId()).stream())
                .toList();
        double promedio = calificaciones.stream().mapToInt(Calificacion::getPuntuacion).average().orElse(0);
        return new PublicacionDetalleDTO(toDTO(publicacion), materiales, promedio, calificaciones.size());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<PublicacionDTO> publicacionesPorUsuario(@PathVariable Long idUsuario) {
        return pS.publicacionesPorUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @GetMapping("/buscar")
    public List<PublicacionBusquedaDTO> buscarPublicacionesPorTexto(@RequestParam String texto) {
        return pS.buscarPublicacionesPorTexto(texto).stream().map(this::toBusquedaDTO).toList();
    }

    @GetMapping("/cercanas")
    public List<PublicacionDistanciaDTO> publicacionesCercanas(@RequestParam Double lat, @RequestParam Double lng, @RequestParam Double radio) {
        return pS.publicacionesCercanas(lat, lng, radio).stream().map(this::toDistanciaDTO).toList();
    }

    @GetMapping("/mapa")
    public List<PublicacionDistanciaDTO> publicacionesMapa(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam Double radioKm,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String tipoPunto
    ) {
        return pS.publicacionesMapa(lat, lng, radioKm, material, categoria, tipoPunto).stream().map(this::toDistanciaDTO).toList();
    }

    @GetMapping("/categoria-material/{categoria}")
    public List<PublicacionCategoriaMaterialDTO> publicacionesPorCategoriaMaterial(@PathVariable String categoria) {
        return pS.publicacionesPorCategoriaMaterial(categoria).stream().map(this::toCategoriaMaterialDTO).toList();
    }

    @GetMapping("/historial/{idUsuario}")
    public List<HistorialMaterialDTO> historialMaterialesUsuario(@PathVariable Long idUsuario) {
        return pS.historialMaterialesUsuario(idUsuario).stream().map(this::toHistorialDTO).toList();
    }

    @PostMapping({"", "/nuevo"})
    public PublicacionDTO insert(@RequestBody PublicacionDTO dto) {
        securityUserResolver.requireAdminOrUser(dto.getIdUsuario());
        return toDTO(pS.insert(toEntity(new Publicacion(), dto)));
    }

    @PostMapping("/{id}/validar")
    public java.util.Map<String, Object> validarInformacion(@PathVariable Long id) {
        Publicacion publicacion = pS.listId(id).orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));
        List<String> errores = new java.util.ArrayList<>();
        validarCampoTexto(publicacion.getTitulo(), "titulo", errores);
        validarCampoTexto(publicacion.getCategoria(), "categoria", errores);
        validarCampoTexto(publicacion.getMaterial(), "material", errores);
        validarCampoTexto(publicacion.getTipoPunto(), "tipoPunto", errores);
        validarCampoTexto(publicacion.getDireccion(), "direccion", errores);
        if (publicacion.getUsuario() == null) {
            errores.add("usuario");
        }
        if (publicacion.getLatitud() == null) {
            errores.add("latitud");
        }
        if (publicacion.getLongitud() == null) {
            errores.add("longitud");
        }
        if (isBlank(publicacion.getDescripcion()) && isBlank(publicacion.getObservaciones())) {
            errores.add("descripcion u observaciones");
        }
        boolean materialesValidos = pMS.materialesPorPublicacion(id).stream()
                .anyMatch(pm -> pm.getCantidad() != null && pm.getCantidad().compareTo(BigDecimal.ZERO) > 0);
        if (!materialesValidos) {
            errores.add("materiales con cantidad mayor a cero");
        }
        boolean valido = publicacion.getTitulo() != null
                && publicacion.getUsuario() != null
                && publicacion.getLatitud() != null
                && publicacion.getLongitud() != null
                && (publicacion.getDescripcion() != null || publicacion.getObservaciones() != null)
                && errores.isEmpty();
        return java.util.Map.of("idPublicacion", id, "valida", valido, "errores", errores);
    }

    @PutMapping("/{id}/observaciones")
    public PublicacionDTO actualizarObservaciones(@PathVariable Long id, @RequestBody java.util.Map<String, Object> request) {
        Publicacion publicacion = pS.listId(id).orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));
        securityUserResolver.requireAdminOrUser(publicacion.getUsuario().getId());
        publicacion.setObservaciones(ValueReader.asString(request.get("observaciones")));
        pS.update(publicacion);
        return toDTO(publicacion);
    }

    @PutMapping("/{id}/evidencia")
    public PublicacionDTO adjuntarEvidencia(@PathVariable Long id, @RequestBody java.util.Map<String, Object> request) {
        Publicacion publicacion = pS.listId(id).orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));
        securityUserResolver.requireAdminOrUser(publicacion.getUsuario().getId());
        publicacion.setImagenesJson(ValueReader.asString(request.get("imagenesJson")));
        pS.update(publicacion);
        return toDTO(publicacion);
    }

    @PutMapping("/{id}/fecha-disponibilidad")
    public PublicacionDTO actualizarFechaDisponibilidad(@PathVariable Long id, @RequestBody java.util.Map<String, Object> request) {
        Publicacion publicacion = pS.listId(id).orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));
        securityUserResolver.requireAdminOrUser(publicacion.getUsuario().getId());
        LocalDate fechaDisponibilidad = LocalDate.parse(request.get("fechaDisponibilidad").toString());
        if (fechaDisponibilidad.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de disponibilidad no puede estar en el pasado");
        }
        publicacion.setFechaDisponibilidad(fechaDisponibilidad);
        pS.update(publicacion);
        return toDTO(publicacion);
    }

    @PutMapping("/{id}")
    public PublicacionDTO update(@PathVariable Long id, @RequestBody PublicacionDTO dto) {
        Publicacion publicacion = pS.listId(id).orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));
        securityUserResolver.requireAdminOrUser(publicacion.getUsuario().getId());
        if (!securityUserResolver.isAdmin()) {
            dto.setIdUsuario(publicacion.getUsuario().getId());
        }
        pS.update(toEntity(publicacion, dto));
        return toDTO(publicacion);
    }

    @PutMapping("/actualiza")
    public PublicacionDTO update(@RequestBody PublicacionDTO dto) {
        return update(dto.getId(), dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        pS.delete(id);
    }

    private Publicacion toEntity(Publicacion publicacion, PublicacionDTO dto) {
        Usuario usuario = uS.listId(dto.getIdUsuario()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        publicacion.setUsuario(usuario);
        publicacion.setTitulo(dto.getTitulo());
        publicacion.setDescripcion(dto.getDescripcion());
        publicacion.setCategoria(dto.getCategoria());
        publicacion.setMaterial(dto.getMaterial());
        publicacion.setTipoPunto(dto.getTipoPunto());
        publicacion.setDireccion(dto.getDireccion());
        publicacion.setLatitud(dto.getLatitud());
        publicacion.setLongitud(dto.getLongitud());
        publicacion.setActivo(dto.getActivo());
        if (dto.getCreatedAt() != null) {
            publicacion.setCreatedAt(dto.getCreatedAt());
        }
        return publicacion;
    }

    private PublicacionDTO toDTO(Publicacion publicacion) {
        ModelMapper m = new ModelMapper();
        PublicacionDTO dto = m.map(publicacion, PublicacionDTO.class);
        dto.setIdUsuario(publicacion.getUsuario().getId());
        dto.setUsuarioNombre(publicacion.getUsuario().getNombre());
        return dto;
    }

    private PublicacionBusquedaDTO toBusquedaDTO(Object[] row) {
        return new PublicacionBusquedaDTO(
                ValueReader.asLong(row[0]),
                ValueReader.asString(row[1]),
                ValueReader.asString(row[2]),
                ValueReader.asString(row[3]),
                ValueReader.asString(row[4]),
                ValueReader.asString(row[5]),
                ValueReader.asString(row[6]),
                ValueReader.asDouble(row[7]),
                ValueReader.asDouble(row[8]),
                ValueReader.asLocalDateTime(row[9]),
                ValueReader.asString(row[10])
        );
    }

    private PublicacionDistanciaDTO toDistanciaDTO(Object[] row) {
        return new PublicacionDistanciaDTO(
                ValueReader.asLong(row[0]),
                ValueReader.asString(row[1]),
                ValueReader.asString(row[2]),
                ValueReader.asString(row[3]),
                ValueReader.asString(row[4]),
                ValueReader.asString(row[5]),
                ValueReader.asString(row[6]),
                ValueReader.asDouble(row[7]),
                ValueReader.asDouble(row[8]),
                ValueReader.asDouble(row[9])
        );
    }

    private PublicacionCategoriaMaterialDTO toCategoriaMaterialDTO(Object[] row) {
        return new PublicacionCategoriaMaterialDTO(
                ValueReader.asString(row[0]),
                ValueReader.asString(row[1]),
                ValueReader.asLong(row[2]),
                ValueReader.asLocalDateTime(row[3])
        );
    }

    private HistorialMaterialDTO toHistorialDTO(Object[] row) {
        return new HistorialMaterialDTO(
                ValueReader.asString(row[0]),
                ValueReader.asString(row[1]),
                ValueReader.asLong(row[2]),
                ValueReader.asLocalDateTime(row[3])
        );
    }

    private PublicacionMaterialDetalleDTO toMaterialDetalleDTO(PublicacionMaterial publicacionMaterial) {
        BigDecimal puntosPorKg = publicacionMaterial.getMaterial().getPuntosPorKg();
        BigDecimal puntosEstimados = puntosPorKg == null ? BigDecimal.ZERO : publicacionMaterial.getCantidad().multiply(puntosPorKg);
        return new PublicacionMaterialDetalleDTO(
                publicacionMaterial.getMaterial().getId(),
                publicacionMaterial.getMaterial().getNombre(),
                publicacionMaterial.getMaterial().getCategoria(),
                publicacionMaterial.getCantidad(),
                publicacionMaterial.getUnidad(),
                puntosPorKg,
                puntosEstimados
        );
    }

    private void validarCampoTexto(String value, String field, List<String> errores) {
        if (isBlank(value)) {
            errores.add(field);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
