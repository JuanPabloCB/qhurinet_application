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
import pe.edu.upc.qhurinet.dtos.ReclamoDTO;
import pe.edu.upc.qhurinet.entities.Reclamo;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.IReclamoService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reclamos")
public class ReclamoController {
    @Autowired
    private IReclamoService rS;

    @Autowired
    private IUsuarioService uS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<ReclamoDTO>> listar() {
        List<Reclamo> reclamos = securityUserResolver.isAdmin()
                ? rS.list()
                : rS.reclamosPorUsuario(securityUserResolver.currentUser().getId());
        List<ReclamoDTO> lista = reclamos.stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> reclamosPorUsuario(@PathVariable Long idUsuario) {
        securityUserResolver.requireAdminOrUser(idUsuario);
        List<ReclamoDTO> lista = rS.reclamosPorUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay reclamos registrados");
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> registrar(@RequestBody ReclamoDTO dto) {
        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        securityUserResolver.requireAdminOrUser(usuario.get().getId());

        ModelMapper m = new ModelMapper();
        Reclamo reclamo = m.map(dto, Reclamo.class);
        reclamo.setUsuario(usuario.get());

        Reclamo cur = rS.insert(reclamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(cur));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Reclamo> reclamo = rS.listId(id);
        if (reclamo.isPresent()) {
            securityUserResolver.requireAdminOrUser(reclamo.get().getUsuario().getId());
            return ResponseEntity.ok(toDTO(reclamo.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reclamo no encontrado");
    }

    @PutMapping("/actualiza")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> actualizar(@RequestBody ReclamoDTO dto) {
        Optional<Reclamo> existente = rS.listId(dto.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reclamo no encontrado");
        }
        securityUserResolver.requireAdminOrUser(existente.get().getUsuario().getId());

        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        securityUserResolver.requireAdminOrUser(usuario.get().getId());

        Reclamo reclamo = existente.get();
        reclamo.setUsuario(usuario.get());
        reclamo.setAsunto(dto.getAsunto());
        reclamo.setDescripcion(dto.getDescripcion());
        reclamo.setEvidenciaUrl(dto.getEvidenciaUrl());
        reclamo.setEstado(dto.getEstado());
        rS.update(reclamo);

        return ResponseEntity.ok("Reclamo actualizado correctamente");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> actualizarPorId(@PathVariable Long id, @RequestBody ReclamoDTO dto) {
        dto.setId(id);
        return actualizar(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<Reclamo> reclamo = rS.listId(id);
        if (reclamo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reclamo no encontrado");
        }
        rS.delete(id);
        return ResponseEntity.ok("Reclamo eliminado correctamente");
    }

    private ReclamoDTO toDTO(Reclamo reclamo) {
        ModelMapper m = new ModelMapper();
        ReclamoDTO dto = m.map(reclamo, ReclamoDTO.class);
        dto.setIdUsuario(reclamo.getUsuario().getId());
        return dto;
    }
}
