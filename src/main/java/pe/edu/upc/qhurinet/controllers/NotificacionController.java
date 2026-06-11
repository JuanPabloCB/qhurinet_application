package pe.edu.upc.qhurinet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.NotificacionDTO;
import pe.edu.upc.qhurinet.entities.Notificacion;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.INotificacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    @Autowired
    private INotificacionService nS;

    @Autowired
    private IUsuarioService uS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<NotificacionDTO>> listar() {
        List<Notificacion> notificaciones = securityUserResolver.isAdmin()
                ? nS.list()
                : nS.notificacionesPorUsuario(securityUserResolver.currentUser().getId());
        List<NotificacionDTO> lista = notificaciones.stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> notificacionesUsuario(@PathVariable Long idUsuario) {
        securityUserResolver.requireAdminOrUser(idUsuario);
        List<NotificacionDTO> lista = nS.notificacionesPorUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay notificaciones registradas");
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> enviar(@RequestBody NotificacionDTO dto) {
        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        securityUserResolver.requireAdminOrUser(usuario.get().getId());

        ModelMapper m = new ModelMapper();
        Notificacion notificacion = m.map(dto, Notificacion.class);
        notificacion.setUsuario(usuario.get());

        Notificacion cur = nS.insert(notificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(cur));
    }

    @PostMapping("/push")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> enviarPush(@RequestBody NotificacionDTO dto) {
        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        securityUserResolver.requireAdminOrUser(usuario.get().getId());
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario.get());
        notificacion.setTipo(dto.getTipo() == null ? "push" : dto.getTipo());
        notificacion.setTitulo(dto.getTitulo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setEstado("enviada");
        Notificacion cur = nS.insert(notificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("enviada", true, "notificacion", toDTO(cur)));
    }

    @PostMapping("/logros/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> generarLogro(@PathVariable Long idUsuario) {
        securityUserResolver.requireAdminOrUser(idUsuario);
        Usuario usuario = uS.listId(idUsuario).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        String nivel = usuario.getNivelParticipacion() == null ? calcularNivel(usuario.getPuntosTotales()) : usuario.getNivelParticipacion();
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTipo("logro");
        notificacion.setTitulo("Nivel de participacion " + nivel);
        notificacion.setMensaje("Tu perfil se encuentra en nivel " + nivel + " con " + (usuario.getPuntosTotales() == null ? 0 : usuario.getPuntosTotales()) + " puntos");
        notificacion.setEstado("pendiente");
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(nS.insert(notificacion)));
    }

    @PutMapping("/{id}/leida")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> marcarLeida(@PathVariable Long id) {
        Optional<Notificacion> notificacion = nS.listId(id);
        if (notificacion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificacion no encontrada");
        }

        Notificacion n = notificacion.get();
        securityUserResolver.requireAdminOrUser(n.getUsuario().getId());
        n.setEstado("leida");
        nS.update(n);
        return ResponseEntity.ok(toDTO(n));
    }

    private NotificacionDTO toDTO(Notificacion notificacion) {
        ModelMapper m = new ModelMapper();
        NotificacionDTO dto = m.map(notificacion, NotificacionDTO.class);
        dto.setIdUsuario(notificacion.getUsuario().getId());
        return dto;
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
