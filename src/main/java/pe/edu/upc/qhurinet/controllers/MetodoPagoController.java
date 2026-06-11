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
import pe.edu.upc.qhurinet.dtos.MetodoPagoDTO;
import pe.edu.upc.qhurinet.entities.MetodoPago;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.IMetodoPagoService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metodos-pago")
public class MetodoPagoController {
    @Autowired
    private IMetodoPagoService mS;

    @Autowired
    private IUsuarioService uS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<MetodoPagoDTO>> listar() {
        List<MetodoPago> metodos = securityUserResolver.isAdmin()
                ? mS.list()
                : mS.metodosPorUsuario(securityUserResolver.currentUser().getId());
        List<MetodoPagoDTO> lista = metodos.stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> metodosPorUsuario(@PathVariable Long idUsuario) {
        securityUserResolver.requireAdminOrUser(idUsuario);
        List<MetodoPagoDTO> lista = mS.metodosPorUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay metodos de pago registrados");
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> registrar(@RequestBody MetodoPagoDTO dto) {
        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        securityUserResolver.requireAdminOrUser(usuario.get().getId());

        ModelMapper m = new ModelMapper();
        MetodoPago metodo = m.map(dto, MetodoPago.class);
        metodo.setUsuario(usuario.get());

        MetodoPago cur = mS.insert(metodo);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(cur));
    }

    @PutMapping("/actualiza")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> actualizar(@RequestBody MetodoPagoDTO dto) {
        Optional<MetodoPago> existente = mS.listId(dto.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Metodo de pago no encontrado");
        }
        securityUserResolver.requireAdminOrUser(existente.get().getUsuario().getId());

        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        securityUserResolver.requireAdminOrUser(usuario.get().getId());

        MetodoPago metodo = existente.get();
        metodo.setUsuario(usuario.get());
        metodo.setTipo(dto.getTipo());
        metodo.setAlias(dto.getAlias());
        metodo.setTitular(dto.getTitular());
        metodo.setDetalleEnmascarado(dto.getDetalleEnmascarado());
        metodo.setPrincipal(dto.getPrincipal());
        metodo.setActivo(dto.getActivo());
        mS.update(metodo);

        return ResponseEntity.ok("Metodo de pago actualizado correctamente");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> actualizarPorId(@PathVariable Long id, @RequestBody MetodoPagoDTO dto) {
        dto.setId(id);
        return actualizar(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<MetodoPago> metodo = mS.listId(id);
        if (metodo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Metodo de pago no encontrado");
        }
        securityUserResolver.requireAdminOrUser(metodo.get().getUsuario().getId());
        mS.delete(id);
        return ResponseEntity.ok("Metodo de pago eliminado correctamente");
    }

    private MetodoPagoDTO toDTO(MetodoPago metodo) {
        ModelMapper m = new ModelMapper();
        MetodoPagoDTO dto = m.map(metodo, MetodoPagoDTO.class);
        dto.setIdUsuario(metodo.getUsuario().getId());
        return dto;
    }
}
