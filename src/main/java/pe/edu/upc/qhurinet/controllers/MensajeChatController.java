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
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.MensajeChatDTO;
import pe.edu.upc.qhurinet.entities.MensajeChat;
import pe.edu.upc.qhurinet.entities.Recoleccion;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.IMensajeChatService;
import pe.edu.upc.qhurinet.servicesinterfaces.IRecoleccionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes-chat")
@PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
public class MensajeChatController {
    @Autowired
    private IMensajeChatService mS;

    @Autowired
    private IUsuarioService uS;

    @Autowired
    private IRecoleccionService rS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasRole('ADMIN')")
    public List<MensajeChatDTO> list() {
        return mS.list().stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public MensajeChatDTO listId(@PathVariable Long id) {
        MensajeChat mensaje = mS.listId(id).orElseThrow(() -> new IllegalArgumentException("Mensaje no encontrado"));
        securityUserResolver.requireAdminOrAnyUser(
                mensaje.getEmisor() == null ? null : mensaje.getEmisor().getId(),
                mensaje.getReceptor() == null ? null : mensaje.getReceptor().getId()
        );
        return toDTO(mensaje);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<MensajeChatDTO> mensajesPorUsuario(@PathVariable Long idUsuario) {
        securityUserResolver.requireAdminOrUser(idUsuario);
        return mS.mensajesPorUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @GetMapping("/conversacion/{idUsuarioUno}/{idUsuarioDos}")
    public List<MensajeChatDTO> conversacion(@PathVariable Long idUsuarioUno, @PathVariable Long idUsuarioDos) {
        securityUserResolver.requireAdminOrAnyUser(idUsuarioUno, idUsuarioDos);
        return mS.conversacion(idUsuarioUno, idUsuarioDos).stream().map(this::toDTO).toList();
    }

    @GetMapping("/recoleccion/{idRecoleccion}")
    public List<MensajeChatDTO> mensajesPorRecoleccion(@PathVariable Long idRecoleccion) {
        Recoleccion recoleccion = rS.listId(idRecoleccion).orElseThrow(() -> new IllegalArgumentException("Recoleccion no encontrada"));
        securityUserResolver.requireAdminOrAnyUser(
                recoleccion.getPublicacion().getUsuario().getId(),
                recoleccion.getRecolector().getId()
        );
        return mS.mensajesPorRecoleccion(idRecoleccion).stream().map(this::toDTO).toList();
    }

    @PostMapping({"", "/nuevo"})
    public MensajeChatDTO insert(@RequestBody MensajeChatDTO dto) {
        securityUserResolver.requireAdminOrAnyUser(dto.getIdEmisor(), dto.getIdReceptor());
        return toDTO(mS.insert(toEntity(new MensajeChat(), dto)));
    }

    @PutMapping("/{id}")
    public MensajeChatDTO update(@PathVariable Long id, @RequestBody MensajeChatDTO dto) {
        MensajeChat mensaje = mS.listId(id).orElseThrow(() -> new IllegalArgumentException("Mensaje no encontrado"));
        securityUserResolver.requireAdminOrAnyUser(
                mensaje.getEmisor() == null ? null : mensaje.getEmisor().getId(),
                mensaje.getReceptor() == null ? null : mensaje.getReceptor().getId()
        );
        mS.update(toEntity(mensaje, dto));
        return toDTO(mensaje);
    }

    @PutMapping("/actualiza")
    public MensajeChatDTO update(@RequestBody MensajeChatDTO dto) {
        return update(dto.getId(), dto);
    }

    @PutMapping("/{id}/leido")
    public MensajeChatDTO marcarLeido(@PathVariable Long id) {
        MensajeChat mensaje = mS.listId(id).orElseThrow(() -> new IllegalArgumentException("Mensaje no encontrado"));
        securityUserResolver.requireAdminOrAnyUser(
                mensaje.getEmisor() == null ? null : mensaje.getEmisor().getId(),
                mensaje.getReceptor() == null ? null : mensaje.getReceptor().getId()
        );
        mensaje.setLeido(true);
        mS.update(mensaje);
        return toDTO(mensaje);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        MensajeChat mensaje = mS.listId(id).orElseThrow(() -> new IllegalArgumentException("Mensaje no encontrado"));
        securityUserResolver.requireAdminOrAnyUser(
                mensaje.getEmisor() == null ? null : mensaje.getEmisor().getId(),
                mensaje.getReceptor() == null ? null : mensaje.getReceptor().getId()
        );
        mS.delete(id);
    }

    private MensajeChat toEntity(MensajeChat mensaje, MensajeChatDTO dto) {
        Usuario emisor = uS.listId(dto.getIdEmisor()).orElseThrow(() -> new IllegalArgumentException("Emisor no encontrado"));
        Usuario receptor = uS.listId(dto.getIdReceptor()).orElseThrow(() -> new IllegalArgumentException("Receptor no encontrado"));
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);
        if (dto.getIdRecoleccion() != null) {
            Recoleccion recoleccion = rS.listId(dto.getIdRecoleccion()).orElseThrow(() -> new IllegalArgumentException("Recoleccion no encontrada"));
            securityUserResolver.requireAdminOrAnyUser(
                    recoleccion.getPublicacion().getUsuario().getId(),
                    recoleccion.getRecolector().getId()
            );
            mensaje.setRecoleccion(recoleccion);
        }
        mensaje.setContenido(dto.getContenido());
        mensaje.setLeido(dto.getLeido());
        if (dto.getCreatedAt() != null) {
            mensaje.setCreatedAt(dto.getCreatedAt());
        }
        return mensaje;
    }

    private MensajeChatDTO toDTO(MensajeChat mensaje) {
        ModelMapper m = new ModelMapper();
        MensajeChatDTO dto = m.map(mensaje, MensajeChatDTO.class);
        dto.setIdEmisor(mensaje.getEmisor() == null ? null : mensaje.getEmisor().getId());
        dto.setEmisorNombre(mensaje.getEmisor() == null ? null : mensaje.getEmisor().getNombre());
        dto.setIdReceptor(mensaje.getReceptor() == null ? null : mensaje.getReceptor().getId());
        dto.setReceptorNombre(mensaje.getReceptor() == null ? null : mensaje.getReceptor().getNombre());
        dto.setIdRecoleccion(mensaje.getRecoleccion() == null ? null : mensaje.getRecoleccion().getId());
        return dto;
    }
}
