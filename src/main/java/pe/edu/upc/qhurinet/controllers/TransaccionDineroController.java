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
import pe.edu.upc.qhurinet.dtos.TransaccionDineroDTO;
import pe.edu.upc.qhurinet.entities.TransaccionDinero;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.servicesinterfaces.ITransaccionDineroService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transacciones-dinero")
public class TransaccionDineroController {
    @Autowired
    private ITransaccionDineroService tS;

    @Autowired
    private IUsuarioService uS;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TransaccionDineroDTO>> listar() {
        List<TransaccionDineroDTO> lista = tS.list().stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> historial(@PathVariable Long idUsuario) {
        List<TransaccionDineroDTO> lista = tS.historialUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay registros");
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrar(@RequestBody TransaccionDineroDTO dto) {
        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        ModelMapper m = new ModelMapper();
        TransaccionDinero transaccion = m.map(dto, TransaccionDinero.class);
        transaccion.setUsuario(usuario.get());

        TransaccionDinero cur = tS.insert(transaccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(cur));
    }

    @PutMapping("/actualiza")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizar(@RequestBody TransaccionDineroDTO dto) {
        Optional<TransaccionDinero> existente = tS.listId(dto.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaccion dinero no encontrada");
        }

        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        TransaccionDinero transaccion = existente.get();
        transaccion.setUsuario(usuario.get());
        transaccion.setTipo(dto.getTipo());
        transaccion.setMonto(dto.getMonto());
        transaccion.setMoneda(dto.getMoneda());
        transaccion.setEstado(dto.getEstado());
        transaccion.setConcepto(dto.getConcepto());
        transaccion.setMetodoPagoTipo(dto.getMetodoPagoTipo());
        transaccion.setMetodoPagoDetalle(dto.getMetodoPagoDetalle());
        transaccion.setReferenciaExterna(dto.getReferenciaExterna());
        tS.update(transaccion);

        return ResponseEntity.ok("Transaccion dinero actualizada correctamente");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarPorId(@PathVariable Long id, @RequestBody TransaccionDineroDTO dto) {
        dto.setId(id);
        return actualizar(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<TransaccionDinero> transaccion = tS.listId(id);
        if (transaccion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaccion dinero no encontrada");
        }
        tS.delete(id);
        return ResponseEntity.ok("Transaccion dinero eliminada correctamente");
    }

    private TransaccionDineroDTO toDTO(TransaccionDinero transaccion) {
        ModelMapper m = new ModelMapper();
        TransaccionDineroDTO dto = m.map(transaccion, TransaccionDineroDTO.class);
        dto.setIdUsuario(transaccion.getUsuario().getId());
        return dto;
    }
}
