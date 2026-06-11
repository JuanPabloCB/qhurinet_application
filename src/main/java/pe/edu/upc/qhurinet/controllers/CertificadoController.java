package pe.edu.upc.qhurinet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import pe.edu.upc.qhurinet.dtos.CertificadoDTO;
import pe.edu.upc.qhurinet.entities.Certificado;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.servicesinterfaces.ICertificadoService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/certificados")
@PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
public class CertificadoController {
    @Autowired
    private ICertificadoService cS;

    @Autowired
    private IUsuarioService uS;

    @GetMapping({"", "/lista"})
    public List<CertificadoDTO> list() {
        return cS.list().stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public CertificadoDTO listId(@PathVariable Long id) {
        return toDTO(cS.listId(id).orElseThrow(() -> new IllegalArgumentException("Certificado no encontrado")));
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<CertificadoDTO> certificadosPorUsuario(@PathVariable Long idUsuario) {
        return cS.certificadosPorUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @GetMapping("/dificultad/{nivelDificultad}")
    public List<CertificadoDTO> certificadosPorDificultad(@PathVariable String nivelDificultad) {
        return cS.certificadosPorDificultad(nivelDificultad).stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> certificadoDigital(@PathVariable Long id) {
        Certificado certificado = cS.listId(id).orElseThrow(() -> new IllegalArgumentException("Certificado no encontrado"));
        String contenido = "Certificado QhuriNet\n" + certificado.getNombre() + "\nUsuario: " + certificado.getUsuario().getNombre();
        byte[] archivo = contenido.getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificado-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(archivo);
    }

    @PostMapping({"", "/nuevo"})
    public CertificadoDTO insert(@RequestBody CertificadoDTO dto) {
        return toDTO(cS.insert(toEntity(new Certificado(), dto)));
    }

    @PutMapping("/{id}")
    public CertificadoDTO update(@PathVariable Long id, @RequestBody CertificadoDTO dto) {
        Certificado certificado = cS.listId(id).orElseThrow(() -> new IllegalArgumentException("Certificado no encontrado"));
        cS.update(toEntity(certificado, dto));
        return toDTO(certificado);
    }

    @PutMapping("/actualiza")
    public CertificadoDTO update(@RequestBody CertificadoDTO dto) {
        return update(dto.getId(), dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        cS.delete(id);
    }

    private Certificado toEntity(Certificado certificado, CertificadoDTO dto) {
        Usuario usuario = uS.listId(dto.getIdUsuario()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        certificado.setUsuario(usuario);
        certificado.setNombre(dto.getNombre());
        certificado.setDescripcion(dto.getDescripcion());
        certificado.setNivelDificultad(dto.getNivelDificultad());
        certificado.setPuntosRequeridos(dto.getPuntosRequeridos());
        certificado.setUrlPdf(dto.getUrlPdf());
        if (dto.getFechaObtencion() != null) {
            certificado.setFechaObtencion(dto.getFechaObtencion());
        }
        return certificado;
    }

    private CertificadoDTO toDTO(Certificado certificado) {
        ModelMapper m = new ModelMapper();
        CertificadoDTO dto = m.map(certificado, CertificadoDTO.class);
        dto.setIdUsuario(certificado.getUsuario().getId());
        dto.setUsuarioNombre(certificado.getUsuario().getNombre());
        return dto;
    }
}
