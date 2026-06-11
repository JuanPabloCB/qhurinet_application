package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.DocumentoVerificacion;

import java.util.List;
import java.util.Optional;

public interface IDocumentoVerificacionService {
    public List<DocumentoVerificacion> list();
    public DocumentoVerificacion insert(DocumentoVerificacion d);
    public Optional<DocumentoVerificacion> listId(Long id);
    public void update(DocumentoVerificacion d);
    public void delete(Long id);
    public List<DocumentoVerificacion> documentosPorUsuario(Long idUsuario);
    public List<DocumentoVerificacion> documentosPorEstado(String estado);
    public DocumentoVerificacion revisar(Long id, String estado, String motivoRechazo);
}
