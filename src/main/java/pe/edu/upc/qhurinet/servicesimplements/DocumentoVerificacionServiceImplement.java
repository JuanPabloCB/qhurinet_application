package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.DocumentoVerificacion;
import pe.edu.upc.qhurinet.repositories.IDocumentoVerificacionRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IDocumentoVerificacionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentoVerificacionServiceImplement implements IDocumentoVerificacionService {
    @Autowired
    private IDocumentoVerificacionRepository dR;

    @Override
    public List<DocumentoVerificacion> list() {
        return dR.findAll();
    }

    @Override
    public DocumentoVerificacion insert(DocumentoVerificacion d) {
        return dR.save(d);
    }

    @Override
    public Optional<DocumentoVerificacion> listId(Long id) {
        return dR.findById(id);
    }

    @Override
    public void update(DocumentoVerificacion d) {
        dR.save(d);
    }

    @Override
    public void delete(Long id) {
        dR.deleteById(id);
    }

    @Override
    public List<DocumentoVerificacion> documentosPorUsuario(Long idUsuario) {
        return dR.findByUsuarioIdOrderByCreatedAtDesc(idUsuario);
    }

    @Override
    public List<DocumentoVerificacion> documentosPorEstado(String estado) {
        return dR.findByEstadoOrderByCreatedAtAsc(estado);
    }

    @Override
    public DocumentoVerificacion revisar(Long id, String estado, String motivoRechazo) {
        DocumentoVerificacion documento = dR.findById(id).orElseThrow(() -> new IllegalArgumentException("Documento no encontrado"));
        documento.setEstado(estado);
        documento.setMotivoRechazo(motivoRechazo);
        documento.setReviewedAt(LocalDateTime.now());
        return dR.save(documento);
    }
}
