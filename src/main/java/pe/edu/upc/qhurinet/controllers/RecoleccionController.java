package pe.edu.upc.qhurinet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.CalificacionDTO;
import pe.edu.upc.qhurinet.dtos.PublicacionDTO;
import pe.edu.upc.qhurinet.dtos.PublicacionMaterialDetalleDTO;
import pe.edu.upc.qhurinet.dtos.RecoleccionDetalleDTO;
import pe.edu.upc.qhurinet.dtos.RecoleccionDTO;
import pe.edu.upc.qhurinet.dtos.UbicacionRecolectorHistorialDTO;
import pe.edu.upc.qhurinet.entities.Calificacion;
import pe.edu.upc.qhurinet.entities.Notificacion;
import pe.edu.upc.qhurinet.entities.Publicacion;
import pe.edu.upc.qhurinet.entities.PublicacionMaterial;
import pe.edu.upc.qhurinet.entities.Recoleccion;
import pe.edu.upc.qhurinet.entities.TransaccionPuntos;
import pe.edu.upc.qhurinet.entities.UbicacionRecolectorHistorial;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.ICalificacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.INotificacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IPublicacionMaterialService;
import pe.edu.upc.qhurinet.servicesinterfaces.IPublicacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IRecoleccionService;
import pe.edu.upc.qhurinet.servicesinterfaces.ITransaccionPuntosService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUbicacionRecolectorHistorialService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recolecciones")
public class RecoleccionController {
    @Autowired
    private IRecoleccionService rS;

    @Autowired
    private IPublicacionService pS;

    @Autowired
    private IUsuarioService uS;

    @Autowired
    private IPublicacionMaterialService pMS;

    @Autowired
    private ITransaccionPuntosService tS;

    @Autowired
    private ICalificacionService cS;

    @Autowired
    private IUbicacionRecolectorHistorialService uHS;

    @Autowired
    private INotificacionService nS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<RecoleccionDTO>> listar() {
        List<RecoleccionDTO> lista = rS.list().stream()
                .filter(this::puedeVerRecoleccion)
                .map(this::toDTO)
                .collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> historialActividades(
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long idUsuario,
            @org.springframework.web.bind.annotation.RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @org.springframework.web.bind.annotation.RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String estado,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String material
    ) {
        if (!securityUserResolver.isAdmin()) {
            idUsuario = securityUserResolver.currentUser().getId();
        }
        List<RecoleccionDetalleDTO> lista = rS.historialActividades(idUsuario, fechaInicio, fechaFin, estado, material)
                .stream()
                .map(this::toDetalleDTO)
                .toList();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Recoleccion> recoleccion = rS.listId(id);
        if (recoleccion.isPresent()) {
            requireParticipante(recoleccion.get());
            return ResponseEntity.ok(toDTO(recoleccion.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
    }

    @GetMapping("/{id}/detalle")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Recoleccion> recoleccion = rS.listId(id);
        if (recoleccion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }
        requireParticipante(recoleccion.get());
        return ResponseEntity.ok(toDetalleDTO(recoleccion.get()));
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> registrar(@RequestBody RecoleccionDTO dto) {
        Optional<Publicacion> publicacion = pS.listId(dto.getIdPublicacion());
        if (publicacion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publicacion no encontrada");
        }

        Optional<Usuario> recolector = uS.listId(dto.getIdRecolector());
        if (recolector.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recolector no encontrado");
        }
        securityUserResolver.requireAdminOrAnyUser(publicacion.get().getUsuario().getId(), recolector.get().getId());

        ModelMapper m = new ModelMapper();
        Recoleccion recoleccion = m.map(dto, Recoleccion.class);
        recoleccion.setPublicacion(publicacion.get());
        recoleccion.setRecolector(recolector.get());

        Recoleccion cur = rS.insert(recoleccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(cur));
    }

    @PutMapping("/actualiza")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> actualizar(@RequestBody RecoleccionDTO dto) {
        Optional<Recoleccion> existente = rS.listId(dto.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }
        requireParticipante(existente.get());
        if (!securityUserResolver.isAdmin()) {
            dto.setIdPublicacion(existente.get().getPublicacion().getId());
            dto.setIdRecolector(existente.get().getRecolector().getId());
        }

        Optional<Publicacion> publicacion = pS.listId(dto.getIdPublicacion());
        if (publicacion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publicacion no encontrada");
        }

        Optional<Usuario> recolector = uS.listId(dto.getIdRecolector());
        if (recolector.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recolector no encontrado");
        }

        Recoleccion recoleccion = existente.get();
        validarRecoleccionEditable(recoleccion);
        recoleccion.setPublicacion(publicacion.get());
        recoleccion.setRecolector(recolector.get());
        recoleccion.setEstado(dto.getEstado());
        recoleccion.setFechaProgramada(dto.getFechaProgramada());
        recoleccion.setFechaCompletada(dto.getFechaCompletada());
        recoleccion.setPrioritaria(dto.getPrioritaria());
        recoleccion.setCodigoQr(dto.getCodigoQr());
        recoleccion.setQrValidado(dto.getQrValidado());
        recoleccion.setLatRecolector(dto.getLatRecolector());
        recoleccion.setLngRecolector(dto.getLngRecolector());
        recoleccion.setIncidenciaDescripcion(dto.getIncidenciaDescripcion());
        recoleccion.setIncidenciaEvidenciaUrl(dto.getIncidenciaEvidenciaUrl());
        rS.update(recoleccion);

        return ResponseEntity.ok("Recoleccion actualizada correctamente");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> actualizarPorId(@PathVariable Long id, @RequestBody RecoleccionDTO dto) {
        dto.setId(id);
        return actualizar(dto);
    }

    @PutMapping("/{id}/reprogramar")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> reprogramar(@PathVariable Long id, @RequestBody RecoleccionDTO dto) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }

        Recoleccion recoleccion = existente.get();
        requireParticipante(recoleccion);
        validarRecoleccionEditable(recoleccion);
        if (dto.getFechaProgramada() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fechaProgramada es requerida");
        }
        if (dto.getFechaProgramada().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La fecha programada no puede estar en el pasado");
        }
        recoleccion.setFechaProgramada(dto.getFechaProgramada());
        recoleccion.setEstado("programada");
        rS.update(recoleccion);
        return ResponseEntity.ok(toDTO(recoleccion));
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }

        Recoleccion recoleccion = existente.get();
        requireParticipante(recoleccion);
        if ("completada".equalsIgnoreCase(recoleccion.getEstado())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede cancelar una recoleccion completada");
        }
        if ("cancelada".equalsIgnoreCase(recoleccion.getEstado())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La recoleccion ya esta cancelada");
        }
        recoleccion.setEstado("cancelada");
        rS.update(recoleccion);
        Publicacion publicacion = recoleccion.getPublicacion();
        publicacion.setEstado("activa");
        publicacion.setActivo(true);
        pS.update(publicacion);
        return ResponseEntity.ok(toDTO(recoleccion));
    }

    @PutMapping("/{id}/prioridad")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> marcarPrioritaria(@PathVariable Long id, @RequestBody RecoleccionDTO dto) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }

        Recoleccion recoleccion = existente.get();
        requireParticipante(recoleccion);
        recoleccion.setPrioritaria(dto.getPrioritaria() == null || dto.getPrioritaria());
        rS.update(recoleccion);
        return ResponseEntity.ok(toDTO(recoleccion));
    }

    @PutMapping("/{id}/incidencia")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> reportarIncidencia(@PathVariable Long id, @RequestBody RecoleccionDTO dto) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }

        Recoleccion recoleccion = existente.get();
        requireParticipante(recoleccion);
        recoleccion.setIncidenciaDescripcion(dto.getIncidenciaDescripcion());
        recoleccion.setIncidenciaEvidenciaUrl(dto.getIncidenciaEvidenciaUrl());
        recoleccion.setEstado("incidencia");
        rS.update(recoleccion);
        return ResponseEntity.ok(toDTO(recoleccion));
    }

    @GetMapping("/{id}/qr")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> generarQr(@PathVariable Long id) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }

        Recoleccion recoleccion = existente.get();
        requireParticipante(recoleccion);
        if (recoleccion.getCodigoQr() == null) {
            recoleccion.setCodigoQr("QR-RECOLECCION-" + recoleccion.getId());
            rS.update(recoleccion);
        }

        return ResponseEntity.ok(Map.of(
                "idRecoleccion", recoleccion.getId(),
                "codigoQr", recoleccion.getCodigoQr(),
                "qrUrl", "/api/recolecciones/" + recoleccion.getId() + "/qr"
        ));
    }

    @PostMapping("/{id}/validar-qr")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> validarQr(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }

        Recoleccion recoleccion = existente.get();
        requireParticipante(recoleccion);
        boolean valido = recoleccion.getCodigoQr() != null && recoleccion.getCodigoQr().equals(request.get("codigoQr"));
        return ResponseEntity.ok(Map.of("idRecoleccion", id, "valido", valido));
    }

    @PostMapping("/{id}/confirmar-entrega")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    @Transactional
    public ResponseEntity<?> confirmarEntrega(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }

        Recoleccion recoleccion = existente.get();
        requireParticipante(recoleccion);
        if ("cancelada".equalsIgnoreCase(recoleccion.getEstado())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede confirmar una recoleccion cancelada");
        }
        if ("completada".equalsIgnoreCase(recoleccion.getEstado())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La recoleccion ya esta completada");
        }
        if (recoleccion.getCodigoQr() != null && !recoleccion.getCodigoQr().equals(request.get("codigoQr"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("QR invalido");
        }

        BigDecimal puntos = calcularPuntos(recoleccion.getPublicacion().getId());
        Usuario usuario = recoleccion.getPublicacion().getUsuario();
        usuario.setPuntosTotales(usuario.getPuntosTotales() + puntos.intValue());
        usuario.setNivelParticipacion(calcularNivel(usuario.getPuntosTotales()));
        uS.update(usuario);

        recoleccion.setEstado("completada");
        recoleccion.setQrValidado(true);
        recoleccion.setFechaCompletada(LocalDateTime.now());
        rS.update(recoleccion);

        TransaccionPuntos tx = new TransaccionPuntos();
        tx.setUsuario(usuario);
        tx.setTipo("ganado");
        tx.setPuntos(puntos.intValue());
        tx.setMotivo("Recoleccion completada");
        tx.setReferenciaTipo("recoleccion");
        tx.setReferenciaId(recoleccion.getId());
        tS.insert(tx);
        crearNotificacion(usuario, "logro", "Puntos acumulados", "Ganaste " + puntos.intValue() + " puntos por una recoleccion completada");

        return ResponseEntity.ok(Map.of(
                "idRecoleccion", id,
                "estado", "completada",
                "puntosGanados", puntos.intValue(),
                "puntosTotales", usuario.getPuntosTotales()
        ));
    }

    @GetMapping("/{id}/seguimiento")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> seguimiento(@PathVariable Long id) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }

        Recoleccion recoleccion = existente.get();
        requireParticipante(recoleccion);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("idRecoleccion", recoleccion.getId());
        map.put("latRecolector", recoleccion.getLatRecolector());
        map.put("lngRecolector", recoleccion.getLngRecolector());
        map.put("estado", recoleccion.getEstado());
        return ResponseEntity.ok(map);
    }

    @PutMapping("/{id}/ubicacion")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> actualizarUbicacion(@PathVariable Long id, @RequestBody RecoleccionDTO dto) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }
        Recoleccion recoleccion = existente.get();
        securityUserResolver.requireAdminOrUser(recoleccion.getRecolector().getId());
        if (dto.getLatRecolector() == null || dto.getLngRecolector() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("latRecolector y lngRecolector son requeridos");
        }
        recoleccion.setLatRecolector(dto.getLatRecolector());
        recoleccion.setLngRecolector(dto.getLngRecolector());
        rS.update(recoleccion);

        UbicacionRecolectorHistorial ubicacion = new UbicacionRecolectorHistorial();
        ubicacion.setRecoleccion(recoleccion);
        ubicacion.setLatRecolector(dto.getLatRecolector());
        ubicacion.setLngRecolector(dto.getLngRecolector());
        UbicacionRecolectorHistorial cur = uHS.insert(ubicacion);
        return ResponseEntity.ok(toUbicacionDTO(cur));
    }

    @GetMapping("/{id}/ubicacion/historial")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> historialUbicacion(@PathVariable Long id) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }
        requireParticipante(existente.get());
        List<UbicacionRecolectorHistorialDTO> lista = uHS.historialPorRecoleccion(id).stream()
                .map(this::toUbicacionDTO)
                .toList();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{id}/calificacion")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> calificar(@PathVariable Long id, @RequestBody CalificacionDTO dto) {
        Optional<Recoleccion> existente = rS.listId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }
        Recoleccion recoleccion = existente.get();
        Long idAutor = dto.getIdAutor() == null ? securityUserResolver.currentUser().getId() : dto.getIdAutor();
        if (!securityUserResolver.isAdmin() && !securityUserResolver.currentUser().getId().equals(idAutor)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes calificar en nombre de otro usuario");
        }
        if (!securityUserResolver.isAdmin()
                && !securityUserResolver.currentUser().getId().equals(recoleccion.getPublicacion().getUsuario().getId())
                && !securityUserResolver.currentUser().getId().equals(recoleccion.getRecolector().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo participantes de la recoleccion pueden calificar");
        }
        if (cS.existeCalificacionPorRecoleccionYAutor(id, idAutor)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La recoleccion ya fue calificada por este usuario");
        }
        if (dto.getPuntuacion() == null || dto.getPuntuacion() < 1 || dto.getPuntuacion() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La puntuacion debe estar entre 1 y 5");
        }
        Usuario autor = uS.listId(idAutor).orElseThrow(() -> new IllegalArgumentException("Autor no encontrado"));
        Calificacion calificacion = new Calificacion();
        calificacion.setRecoleccion(recoleccion);
        calificacion.setAutor(autor);
        calificacion.setPuntuacion(dto.getPuntuacion());
        calificacion.setComentario(dto.getComentario());
        return ResponseEntity.status(HttpStatus.CREATED).body(toCalificacionDTO(cS.insert(calificacion)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<Recoleccion> recoleccion = rS.listId(id);
        if (recoleccion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }
        rS.delete(id);
        return ResponseEntity.ok("Recoleccion eliminada correctamente");
    }

    private BigDecimal calcularPuntos(Long idPublicacion) {
        return pMS.materialesPorPublicacion(idPublicacion).stream()
                .map(this::puntosPorMaterial)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(0, RoundingMode.DOWN);
    }

    private BigDecimal puntosPorMaterial(PublicacionMaterial publicacionMaterial) {
        return publicacionMaterial.getCantidad().multiply(publicacionMaterial.getMaterial().getPuntosPorKg());
    }

    private RecoleccionDTO toDTO(Recoleccion recoleccion) {
        ModelMapper m = new ModelMapper();
        RecoleccionDTO dto = m.map(recoleccion, RecoleccionDTO.class);
        dto.setIdPublicacion(recoleccion.getPublicacion().getId());
        dto.setIdRecolector(recoleccion.getRecolector().getId());
        return dto;
    }

    private RecoleccionDetalleDTO toDetalleDTO(Recoleccion recoleccion) {
        RecoleccionDetalleDTO dto = new RecoleccionDetalleDTO();
        dto.setRecoleccion(toDTO(recoleccion));
        dto.setPublicacion(toPublicacionDTO(recoleccion.getPublicacion()));
        dto.setIdGenerador(recoleccion.getPublicacion().getUsuario().getId());
        dto.setGeneradorNombre(recoleccion.getPublicacion().getUsuario().getNombre());
        dto.setIdRecolector(recoleccion.getRecolector().getId());
        dto.setRecolectorNombre(recoleccion.getRecolector().getNombre());
        dto.setMateriales(pMS.materialesPorPublicacion(recoleccion.getPublicacion().getId()).stream()
                .map(this::toMaterialDetalleDTO)
                .toList());
        dto.setCalificaciones(cS.calificacionesPorRecoleccion(recoleccion.getId()).stream()
                .map(this::toCalificacionDTO)
                .toList());
        return dto;
    }

    private PublicacionDTO toPublicacionDTO(Publicacion publicacion) {
        ModelMapper m = new ModelMapper();
        PublicacionDTO dto = m.map(publicacion, PublicacionDTO.class);
        dto.setIdUsuario(publicacion.getUsuario().getId());
        dto.setUsuarioNombre(publicacion.getUsuario().getNombre());
        return dto;
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

    private CalificacionDTO toCalificacionDTO(Calificacion calificacion) {
        ModelMapper m = new ModelMapper();
        CalificacionDTO dto = m.map(calificacion, CalificacionDTO.class);
        dto.setIdRecoleccion(calificacion.getRecoleccion().getId());
        dto.setIdAutor(calificacion.getAutor().getId());
        return dto;
    }

    private UbicacionRecolectorHistorialDTO toUbicacionDTO(UbicacionRecolectorHistorial ubicacion) {
        return new UbicacionRecolectorHistorialDTO(
                ubicacion.getId(),
                ubicacion.getRecoleccion().getId(),
                ubicacion.getLatRecolector(),
                ubicacion.getLngRecolector(),
                ubicacion.getCreatedAt()
        );
    }

    private boolean puedeVerRecoleccion(Recoleccion recoleccion) {
        if (securityUserResolver.isAdmin()) {
            return true;
        }
        Long currentUserId = securityUserResolver.currentUser().getId();
        return currentUserId.equals(recoleccion.getRecolector().getId())
                || currentUserId.equals(recoleccion.getPublicacion().getUsuario().getId());
    }

    private void requireParticipante(Recoleccion recoleccion) {
        securityUserResolver.requireAdminOrAnyUser(
                recoleccion.getRecolector().getId(),
                recoleccion.getPublicacion().getUsuario().getId()
        );
    }

    private void validarRecoleccionEditable(Recoleccion recoleccion) {
        if ("completada".equalsIgnoreCase(recoleccion.getEstado()) || "cancelada".equalsIgnoreCase(recoleccion.getEstado())) {
            throw new IllegalArgumentException("La recoleccion no puede modificarse en estado " + recoleccion.getEstado());
        }
    }

    private String calcularNivel(Integer puntosTotales) {
        int puntos = puntosTotales == null ? 0 : puntosTotales;
        if (puntos >= 1000) {
            return "PLATINO";
        }
        if (puntos >= 500) {
            return "ORO";
        }
        if (puntos >= 100) {
            return "PLATA";
        }
        return "BRONCE";
    }

    private void crearNotificacion(Usuario usuario, String tipo, String titulo, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTipo(tipo);
        notificacion.setTitulo(titulo);
        notificacion.setMensaje(mensaje);
        notificacion.setEstado("pendiente");
        nS.insert(notificacion);
    }

}
