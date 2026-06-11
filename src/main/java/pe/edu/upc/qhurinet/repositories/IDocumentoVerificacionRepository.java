package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.qhurinet.entities.DocumentoVerificacion;

import java.util.List;

public interface IDocumentoVerificacionRepository extends JpaRepository<DocumentoVerificacion, Long> {
    List<DocumentoVerificacion> findByUsuarioIdOrderByCreatedAtDesc(Long idUsuario);
    List<DocumentoVerificacion> findByEstadoOrderByCreatedAtAsc(String estado);
}
