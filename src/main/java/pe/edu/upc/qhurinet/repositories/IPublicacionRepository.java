package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.qhurinet.entities.Publicacion;

import java.util.List;

public interface IPublicacionRepository extends JpaRepository<Publicacion, Long> {
    @Query(value = """
        SELECT *
        FROM publicacion
        WHERE id_usuario = ?1
        ORDER BY created_at DESC
        """, nativeQuery = true)
    List<Publicacion> findByUsuarioIdOrderByCreatedAtDesc(Long idUsuario);

    @Query(value = """
        SELECT COUNT(*)
        FROM publicacion
        WHERE id_usuario = ?1
        """, nativeQuery = true)
    long countByUsuarioId(Long idUsuario);

    @Query(value = """
        SELECT p.id, p.titulo, p.descripcion, p.categoria, p.material, p.tipo_punto,
               p.direccion, p.latitud, p.longitud, p.created_at, u.nombre
        FROM publicacion p
        INNER JOIN usuario u ON p.id_usuario = u.id
        WHERE p.activo = true
        AND (
            LOWER(p.titulo) LIKE LOWER(CONCAT('%', ?1, '%'))
            OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', ?1, '%'))
            OR LOWER(p.categoria) LIKE LOWER(CONCAT('%', ?1, '%'))
            OR LOWER(p.material) LIKE LOWER(CONCAT('%', ?1, '%'))
        )
        ORDER BY p.created_at DESC
        """, nativeQuery = true)
    List<Object[]> buscarPublicacionesPorTexto(String texto);

    @Query(value = """
        SELECT p.id, p.titulo, p.descripcion, p.categoria, p.material, p.tipo_punto,
               p.direccion, p.latitud, p.longitud,
               (111.045 * SQRT(POWER(p.latitud - ?1, 2) + POWER(p.longitud - ?2, 2))) AS distancia_km
        FROM publicacion p
        WHERE p.activo = true
        AND (111.045 * SQRT(POWER(p.latitud - ?1, 2) + POWER(p.longitud - ?2, 2))) <= ?3
        ORDER BY distancia_km ASC
        """, nativeQuery = true)
    List<Object[]> publicacionesCercanas(Double lat, Double lng, Double radio);

    @Query(value = """
        SELECT p.id, p.titulo, p.descripcion, p.categoria, p.material, p.tipo_punto,
               p.direccion, p.latitud, p.longitud,
               (111.045 * SQRT(POWER(p.latitud - ?1, 2) + POWER(p.longitud - ?2, 2))) AS distancia_km
        FROM publicacion p
        WHERE p.activo = true
        AND (111.045 * SQRT(POWER(p.latitud - ?1, 2) + POWER(p.longitud - ?2, 2))) <= ?3
        AND (?4 IS NULL OR LOWER(p.material) = LOWER(?4))
        AND (?5 IS NULL OR LOWER(p.categoria) = LOWER(?5))
        AND (?6 IS NULL OR LOWER(p.tipo_punto) = LOWER(?6))
        ORDER BY distancia_km ASC
        """, nativeQuery = true)
    List<Object[]> publicacionesMapa(Double lat, Double lng, Double radioKm, String material, String categoria, String tipoPunto);

    @Query(value = """
        SELECT p.categoria, p.material, COUNT(p.id) AS cantidad, MAX(p.created_at) AS ultima_publicacion
        FROM publicacion p
        WHERE p.categoria = ?1
        GROUP BY p.categoria, p.material
        ORDER BY cantidad DESC, p.material ASC
        """, nativeQuery = true)
    List<Object[]> publicacionesPorCategoriaMaterial(String categoria);

    @Query(value = """
        SELECT p.material, p.categoria, COUNT(p.id) AS cantidad, MAX(p.created_at) AS ultima_publicacion
        FROM publicacion p
        WHERE p.id_usuario = ?1
        GROUP BY p.material, p.categoria
        ORDER BY ultima_publicacion DESC
        """, nativeQuery = true)
    List<Object[]> historialMaterialesUsuario(Long idUsuario);
}
