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
import pe.edu.upc.qhurinet.dtos.DocumentoRevisionDTO;
import pe.edu.upc.qhurinet.dtos.DocumentoVerificacionDTO;
import pe.edu.upc.qhurinet.entities.DocumentoVerificacion;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.servicesinterfaces.IDocumentoVerificacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/documentos-verificacion")
@PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
public class DocumentoVerificacionController {
    @Autowired
    private IDocumentoVerificacionService dS;

    @Autowired
    private IUsuarioService uS;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasRole('ADMIN')")
    public List<DocumentoVerificacionDTO> list() {
        return dS.list().stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public DocumentoVerificacionDTO listId(@PathVariable Long id) {
        return toDTO(dS.listId(id).orElseThrow(() -> new IllegalArgumentException("Documento no encontrado")));
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<DocumentoVerificacionDTO> documentosPorUsuario(@PathVariable Long idUsuario) {
        return dS.documentosPorUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DocumentoVerificacionDTO> documentosPorEstado(@PathVariable String estado) {
        return dS.documentosPorEstado(estado).stream().map(this::toDTO).toList();
    }

    @PostMapping({"", "/nuevo"})
    public DocumentoVerificacionDTO insert(@RequestBody DocumentoVerificacionDTO dto) {
        return toDTO(dS.insert(toEntity(new DocumentoVerificacion(), dto)));
    }

    @PutMapping("/{id}")
    public DocumentoVerificacionDTO update(@PathVariable Long id, @RequestBody DocumentoVerificacionDTO dto) {
        DocumentoVerificacion documento = dS.listId(id).orElseThrow(() -> new IllegalArgumentException("Documento no encontrado"));
        dS.update(toEntity(documento, dto));
        return toDTO(documento);
    }

    @PutMapping("/actualiza")
    public DocumentoVerificacionDTO update(@RequestBody DocumentoVerificacionDTO dto) {
        return update(dto.getId(), dto);
    }

    @PutMapping("/{id}/revisar")
    @PreAuthorize("hasRole('ADMIN')")
    public DocumentoVerificacionDTO revisar(@PathVariable Long id, @RequestBody DocumentoRevisionDTO dto) {
        return toDTO(dS.revisar(id, dto.getEstado(), dto.getMotivoRechazo()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        dS.delete(id);
    }

    private DocumentoVerificacion toEntity(DocumentoVerificacion documento, DocumentoVerificacionDTO dto) {
        Usuario usuario = uS.listId(dto.getIdUsuario()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        documento.setUsuario(usuario);
        documento.setTipo(dto.getTipo());
        documento.setUrlArchivo(dto.getUrlArchivo());
        documento.setEstado(dto.getEstado());
        documento.setMotivoRechazo(dto.getMotivoRechazo());
        if (dto.getCreatedAt() != null) {
            documento.setCreatedAt(dto.getCreatedAt());
        }
        if (dto.getReviewedAt() != null) {
            documento.setReviewedAt(dto.getReviewedAt());
        }
        return documento;
    }

    private DocumentoVerificacionDTO toDTO(DocumentoVerificacion documento) {
        ModelMapper m = new ModelMapper();
        DocumentoVerificacionDTO dto = m.map(documento, DocumentoVerificacionDTO.class);
        dto.setIdUsuario(documento.getUsuario().getId());
        dto.setUsuarioNombre(documento.getUsuario().getNombre());
        return dto;
    }
}
