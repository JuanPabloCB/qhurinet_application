package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.qhurinet.entities.Certificado;

import java.util.List;

public interface ICertificadoRepository extends JpaRepository<Certificado, Long> {
    @Query(value = """
        SELECT *
        FROM certificado
        WHERE id_usuario = ?1
        ORDER BY fecha_obtencion DESC
        """, nativeQuery = true)
    List<Certificado> findByUsuarioIdOrderByFechaObtencionDesc(Long idUsuario);

    @Query(value = """
        SELECT *
        FROM certificado
        WHERE LOWER(nivel_dificultad) = LOWER(?1)
        ORDER BY fecha_obtencion DESC
        """, nativeQuery = true)
    List<Certificado> findByNivelDificultadIgnoreCaseOrderByFechaObtencionDesc(String nivelDificultad);
}
