package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.qhurinet.entities.Calificacion;

import java.util.List;

public interface ICalificacionRepository extends JpaRepository<Calificacion, Long> {
    @Query(value = """
        SELECT c.*
        FROM calificacion c
        INNER JOIN recoleccion r ON c.id_recoleccion = r.id
        WHERE r.id_recolector = ?1
        ORDER BY c.created_at DESC
        """, nativeQuery = true)
    List<Calificacion> findByRecoleccionRecolectorIdOrderByCreatedAtDesc(Long idRecolector);

    List<Calificacion> findByRecoleccionIdOrderByCreatedAtDesc(Long idRecoleccion);

    boolean existsByRecoleccionIdAndAutorId(Long idRecoleccion, Long idAutor);
}
