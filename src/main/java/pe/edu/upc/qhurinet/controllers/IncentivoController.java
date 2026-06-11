package pe.edu.upc.qhurinet.controllers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.IncentivoDTO;
import pe.edu.upc.qhurinet.entities.Incentivo;
import pe.edu.upc.qhurinet.entities.Notificacion;
import pe.edu.upc.qhurinet.entities.TransaccionPuntos;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.entities.UsuarioIncentivo;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.IIncentivoService;
import pe.edu.upc.qhurinet.servicesinterfaces.INotificacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.ITransaccionPuntosService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioIncentivoService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
//Prueba de conexion de rama Oliver
@RestController
@RequestMapping("/api/incentivos")
public class IncentivoController {
    @Autowired
    private IIncentivoService iS;

    @Autowired
    private IUsuarioService uS;

    @Autowired
    private IUsuarioIncentivoService uIS;

    @Autowired
    private ITransaccionPuntosService tS;

    @Autowired
    private INotificacionService nS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<IncentivoDTO>> listar() {
        List<IncentivoDTO> lista = iS.list().stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<IncentivoDTO>> incentivosActivos() {
        List<IncentivoDTO> lista = iS.incentivosActivos().stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/desafios")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<IncentivoDTO>> desafios() {
        List<IncentivoDTO> lista = iS.incentivosActivosPorTipo("desafio").stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/recompensas")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<IncentivoDTO>> recompensas() {
        List<IncentivoDTO> lista = iS.incentivosActivosPorTipo("recompensa").stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Incentivo> incentivo = iS.listId(id);
        if (incentivo.isPresent()) {
            return ResponseEntity.ok(toDTO(incentivo.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incentivo no encontrado");
    }

    @PostMapping("/{id}/canjear/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> canjear(@PathVariable Long id, @PathVariable Long idUsuario) {
        securityUserResolver.requireAdminOrUser(idUsuario);
        Optional<Incentivo> incentivoOptional = iS.listId(id);
        if (incentivoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incentivo no encontrado");
        }

        Optional<Usuario> usuarioOptional = uS.listId(idUsuario);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Incentivo incentivo = incentivoOptional.get();
        Usuario usuario = usuarioOptional.get();
        if (incentivo.getCostoPuntos() == null || usuario.getPuntosTotales() < incentivo.getCostoPuntos()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Puntos insuficientes");
        }
        if (incentivo.getStock() != null && incentivo.getStock() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sin stock disponible");
        }

        usuario.setPuntosTotales(usuario.getPuntosTotales() - incentivo.getCostoPuntos());
        usuario.setNivelParticipacion(calcularNivel(usuario.getPuntosTotales()));
        uS.update(usuario);
        incentivo.setStock(incentivo.getStock() == null ? 0 : incentivo.getStock() - 1);
        iS.update(incentivo);

        UsuarioIncentivo usuarioIncentivo = uIS.findByUsuarioIdAndIncentivoId(idUsuario, id).orElseGet(UsuarioIncentivo::new);
        usuarioIncentivo.setUsuario(usuario);
        usuarioIncentivo.setIncentivo(incentivo);
        usuarioIncentivo.setCantidadActual(1);
        usuarioIncentivo.setEstado("reclamado");
        usuarioIncentivo.setCompletadoEn(java.time.LocalDateTime.now());
        uIS.insert(usuarioIncentivo);

        TransaccionPuntos tx = new TransaccionPuntos();
        tx.setUsuario(usuario);
        tx.setTipo("canje");
        tx.setPuntos(incentivo.getCostoPuntos());
        tx.setMotivo("Canje de incentivo: " + incentivo.getNombre());
        tx.setReferenciaTipo("incentivo");
        tx.setReferenciaId(incentivo.getId());
        tS.insert(tx);
        crearNotificacion(usuario, "recompensa", "Canje realizado", "Canjeaste el incentivo " + incentivo.getNombre());

        return ResponseEntity.ok(Map.of("idUsuario", idUsuario, "idIncentivo", id, "estado", "reclamado", "puntosRestantes", usuario.getPuntosTotales()));
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrar(@RequestBody IncentivoDTO dto) {
        ModelMapper m = new ModelMapper();
        Incentivo incentivo = m.map(dto, Incentivo.class);
        normalizarValores(incentivo);

        Incentivo cur = iS.insert(incentivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(cur));
    }

    @PutMapping("/actualiza")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizar(@RequestBody IncentivoDTO dto) {
        Optional<Incentivo> existente = iS.listId(dto.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incentivo no encontrado");
        }

        Incentivo incentivo = existente.get();
        incentivo.setNombre(dto.getNombre());
        incentivo.setTipo(dto.getTipo());
        incentivo.setMetaCantidad(dto.getMetaCantidad());
        incentivo.setMetaUnidad(dto.getMetaUnidad());
        incentivo.setFechaInicio(dto.getFechaInicio());
        incentivo.setFechaFin(dto.getFechaFin());
        incentivo.setActivo(dto.getActivo());
        normalizarValores(incentivo);
        iS.update(incentivo);

        return ResponseEntity.ok("Incentivo actualizado correctamente");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarPorId(@PathVariable Long id, @RequestBody IncentivoDTO dto) {
        dto.setId(id);
        return actualizar(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<Incentivo> incentivo = iS.listId(id);
        if (incentivo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incentivo no encontrado");
        }
        iS.delete(id);
        return ResponseEntity.ok("Incentivo eliminado correctamente");
    }

    private void normalizarValores(Incentivo incentivo) {
        if (incentivo.getCostoPuntos() == null) {
            incentivo.setCostoPuntos(0);
        }
        if (incentivo.getStock() == null) {
            incentivo.setStock(0);
        }
    }

    private IncentivoDTO toDTO(Incentivo incentivo) {
        ModelMapper m = new ModelMapper();
        return m.map(incentivo, IncentivoDTO.class);
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
}
