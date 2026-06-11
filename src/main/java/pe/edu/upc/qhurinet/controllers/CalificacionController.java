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
import pe.edu.upc.qhurinet.dtos.CalificacionDTO;
import pe.edu.upc.qhurinet.entities.Calificacion;
import pe.edu.upc.qhurinet.entities.Recoleccion;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.ICalificacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IRecoleccionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {
    @Autowired
    private ICalificacionService cS;

    @Autowired
    private IRecoleccionService recoleccionService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CalificacionDTO>> listar() {
        List<CalificacionDTO> lista = cS.list()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> registrar(@RequestBody CalificacionDTO dto) {
        Optional<Recoleccion> recoleccion = recoleccionService.listId(dto.getIdRecoleccion());
        if (recoleccion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }

        Optional<Usuario> usuario = usuarioService.listId(dto.getIdAutor());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor no encontrado");
        }
        if (!securityUserResolver.isAdmin() && !securityUserResolver.currentUser().getId().equals(usuario.get().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes calificar en nombre de otro usuario");
        }
        securityUserResolver.requireAdminOrUser(usuario.get().getId());
        if (!securityUserResolver.isAdmin()
                && !securityUserResolver.currentUser().getId().equals(recoleccion.get().getPublicacion().getUsuario().getId())
                && !securityUserResolver.currentUser().getId().equals(recoleccion.get().getRecolector().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo participantes de la recoleccion pueden calificar");
        }
        if (dto.getPuntuacion() == null || dto.getPuntuacion() < 1 || dto.getPuntuacion() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La puntuacion debe estar entre 1 y 5");
        }
        if (cS.existeCalificacionPorRecoleccionYAutor(dto.getIdRecoleccion(), dto.getIdAutor())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La recoleccion ya fue calificada por este usuario");
        }

        ModelMapper m = new ModelMapper();
        Calificacion c = m.map(dto, Calificacion.class);
        c.setRecoleccion(recoleccion.get());
        c.setAutor(usuario.get());

        Calificacion cur = cS.insert(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(cur));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Calificacion> calificacion = cS.listId(id);
        if (calificacion.isPresent()) {
            requireAutorOParticipante(calificacion.get());
            return ResponseEntity.ok(toDTO(calificacion.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calificacion no encontrada");
    }

    @PutMapping("/actualiza")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> actualizar(@RequestBody CalificacionDTO dto) {
        Optional<Calificacion> existente = cS.listId(dto.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calificacion no encontrada");
        }

        Optional<Recoleccion> recoleccion = recoleccionService.listId(dto.getIdRecoleccion());
        if (recoleccion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recoleccion no encontrada");
        }

        Optional<Usuario> usuario = usuarioService.listId(dto.getIdAutor());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autor no encontrado");
        }
        securityUserResolver.requireAdminOrUser(existente.get().getAutor().getId());
        if (dto.getPuntuacion() == null || dto.getPuntuacion() < 1 || dto.getPuntuacion() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La puntuacion debe estar entre 1 y 5");
        }

        Calificacion c = existente.get();
        c.setRecoleccion(recoleccion.get());
        c.setAutor(usuario.get());
        c.setPuntuacion(dto.getPuntuacion());
        c.setComentario(dto.getComentario());
        cS.update(c);

        return ResponseEntity.ok("Calificacion actualizada correctamente");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> actualizarPorId(@PathVariable Long id, @RequestBody CalificacionDTO dto) {
        dto.setId(id);
        return actualizar(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<Calificacion> calificacion = cS.listId(id);
        if (calificacion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calificacion no encontrada");
        }
        securityUserResolver.requireAdminOrUser(calificacion.get().getAutor().getId());

        cS.delete(id);
        return ResponseEntity.ok("Calificacion eliminada correctamente");
    }

    @GetMapping("/recolector/{idRecolector}/reputacion")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> reputacionRecolector(@PathVariable Long idRecolector) {
        List<Calificacion> calificaciones = cS.calificacionesPorRecolector(idRecolector);
        double promedio = calificaciones.stream().mapToInt(Calificacion::getPuntuacion).average().orElse(0);
        return ResponseEntity.ok(Map.of("idRecolector", idRecolector, "promedio", promedio, "cantidad", calificaciones.size()));
    }

    private CalificacionDTO toDTO(Calificacion calificacion) {
        ModelMapper m = new ModelMapper();
        CalificacionDTO dto = m.map(calificacion, CalificacionDTO.class);
        dto.setIdRecoleccion(calificacion.getRecoleccion().getId());
        dto.setIdAutor(calificacion.getAutor().getId());
        return dto;
    }

    private void requireAutorOParticipante(Calificacion calificacion) {
        securityUserResolver.requireAdminOrAnyUser(
                calificacion.getAutor().getId(),
                calificacion.getRecoleccion().getRecolector().getId(),
                calificacion.getRecoleccion().getPublicacion().getUsuario().getId()
        );
    }
}
