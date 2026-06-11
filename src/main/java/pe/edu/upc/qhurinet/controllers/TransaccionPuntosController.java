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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.TransaccionPuntosDTO;
import pe.edu.upc.qhurinet.entities.TransaccionPuntos;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.ITransaccionPuntosService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transacciones-puntos")
public class TransaccionPuntosController {
    @Autowired
    private ITransaccionPuntosService tS;

    @Autowired
    private IUsuarioService uS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TransaccionPuntosDTO>> listar() {
        List<TransaccionPuntosDTO> lista = tS.list()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrar(@RequestBody TransaccionPuntosDTO dto) {
        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        ModelMapper m = new ModelMapper();
        TransaccionPuntos transaccion = m.map(dto, TransaccionPuntos.class);
        transaccion.setUsuario(usuario.get());

        TransaccionPuntos cur = tS.insert(transaccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(cur));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<TransaccionPuntos> transaccion = tS.listId(id);
        if (transaccion.isPresent()) {
            securityUserResolver.requireAdminOrUser(transaccion.get().getUsuario().getId());
            return ResponseEntity.ok(toDTO(transaccion.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaccion puntos no encontrada");
    }

    @PutMapping("/actualiza")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizar(@RequestBody TransaccionPuntosDTO dto) {
        Optional<TransaccionPuntos> existente = tS.listId(dto.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaccion puntos no encontrada");
        }

        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        TransaccionPuntos transaccion = existente.get();
        transaccion.setUsuario(usuario.get());
        transaccion.setTipo(dto.getTipo());
        transaccion.setPuntos(dto.getPuntos());
        transaccion.setMotivo(dto.getMotivo());
        transaccion.setReferenciaTipo(dto.getReferenciaTipo());
        transaccion.setReferenciaId(dto.getReferenciaId());
        tS.update(transaccion);

        return ResponseEntity.ok("Transaccion puntos actualizada correctamente");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarPorId(@PathVariable Long id, @RequestBody TransaccionPuntosDTO dto) {
        dto.setId(id);
        return actualizar(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<TransaccionPuntos> transaccion = tS.listId(id);
        if (transaccion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaccion puntos no encontrada");
        }

        tS.delete(id);
        return ResponseEntity.ok("Transaccion puntos eliminada correctamente");
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> historial(@PathVariable Long idUsuario, @RequestParam(required = false) String tipo) {
        securityUserResolver.requireAdminOrUser(idUsuario);
        List<TransaccionPuntos> transacciones = tipo == null || tipo.isBlank()
                ? tS.historialUsuario(idUsuario)
                : tS.historialUsuarioPorTipo(idUsuario, tipo);

        List<TransaccionPuntosDTO> lista = transacciones
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay registros");
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/valores")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> valoresPorAccion() {
        return ResponseEntity.ok(List.of(
                Map.of("accion", "plastico", "puntosPorKg", 3),
                Map.of("accion", "carton", "puntosPorKg", 2),
                Map.of("accion", "vidrio", "puntosPorKg", 2),
                Map.of("accion", "metal", "puntosPorKg", 4)
        ));
    }

    private TransaccionPuntosDTO toDTO(TransaccionPuntos transaccion) {
        ModelMapper m = new ModelMapper();
        TransaccionPuntosDTO dto = m.map(transaccion, TransaccionPuntosDTO.class);
        dto.setIdUsuario(transaccion.getUsuario().getId());
        return dto;
    }
}
