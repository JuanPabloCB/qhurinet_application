package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.qhurinet.entities.Recoleccion;

import java.time.LocalDateTime;
import java.util.List;

public interface IRecoleccionRepository extends JpaRepository<Recoleccion, Long> {
    @Query(value = """
        SELECT COUNT(*)
        FROM recoleccion r
        INNER JOIN publicacion p ON r.id_publicacion = p.id
        WHERE r.id_recolector = ?1
           OR p.id_usuario = ?1
        """, nativeQuery = true)
    long countActividadesUsuario(Long idUsuario);

    @Query(value = """
        SELECT DISTINCT r.*
        FROM recoleccion r
        INNER JOIN publicacion p ON r.id_publicacion = p.id
        LEFT JOIN publicacion_material pm ON pm.id_publicacion = p.id
        LEFT JOIN material m ON pm.id_material = m.id
        WHERE (?1 IS NULL OR r.id_recolector = ?1 OR p.id_usuario = ?1)
          AND (?2 IS NULL OR r.fecha_programada >= ?2)
          AND (?3 IS NULL OR r.fecha_programada <= ?3)
          AND (?4 IS NULL OR LOWER(r.estado) = LOWER(?4))
          AND (?5 IS NULL
               OR LOWER(p.material) LIKE LOWER(CONCAT('%', ?5, '%'))
               OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', ?5, '%'))
               OR LOWER(m.categoria) LIKE LOWER(CONCAT('%', ?5, '%')))
        ORDER BY r.fecha_programada DESC, r.created_at DESC
        """, nativeQuery = true)
    List<Recoleccion> historialActividades(
            Long idUsuario,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            String estado,
            String material
    );
}
