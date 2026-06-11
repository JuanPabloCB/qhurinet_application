package pe.edu.upc.qhurinet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.FaqDTO;
import pe.edu.upc.qhurinet.dtos.ReclamoDTO;
import pe.edu.upc.qhurinet.dtos.SoporteContactoDTO;
import pe.edu.upc.qhurinet.entities.Faq;
import pe.edu.upc.qhurinet.entities.Reclamo;
import pe.edu.upc.qhurinet.entities.SoporteContacto;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.servicesinterfaces.IFaqService;
import pe.edu.upc.qhurinet.servicesinterfaces.IReclamoService;
import pe.edu.upc.qhurinet.servicesinterfaces.ISoporteContactoService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/soporte")
public class SoporteController {
    @Autowired
    private IFaqService fS;

    @Autowired
    private ISoporteContactoService sS;

    @Autowired
    private IReclamoService rS;

    @Autowired
    private IUsuarioService uS;

    @GetMapping("/faq")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<FaqDTO>> faq() {
        List<FaqDTO> lista = fS.activos().stream().map(this::toFaqDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/contactos")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<SoporteContactoDTO>> contactos() {
        List<SoporteContactoDTO> lista = sS.activos().stream().map(this::toSoporteDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/tickets")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> crearTicket(@RequestBody ReclamoDTO dto) {
        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        ModelMapper m = new ModelMapper();
        Reclamo reclamo = m.map(dto, Reclamo.class);
        reclamo.setUsuario(usuario.get());
        reclamo.setEstado("abierto");

        Reclamo cur = rS.insert(reclamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(toReclamoDTO(cur));
    }

    private FaqDTO toFaqDTO(Faq faq) {
        ModelMapper m = new ModelMapper();
        return m.map(faq, FaqDTO.class);
    }

    private SoporteContactoDTO toSoporteDTO(SoporteContacto soporte) {
        ModelMapper m = new ModelMapper();
        return m.map(soporte, SoporteContactoDTO.class);
    }

    private ReclamoDTO toReclamoDTO(Reclamo reclamo) {
        ModelMapper m = new ModelMapper();
        ReclamoDTO dto = m.map(reclamo, ReclamoDTO.class);
        dto.setIdUsuario(reclamo.getUsuario().getId());
        return dto;
    }
}
