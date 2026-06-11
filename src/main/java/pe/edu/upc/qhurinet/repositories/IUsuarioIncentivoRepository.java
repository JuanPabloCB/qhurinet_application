package pe.edu.upc.qhurinet.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.qhurinet.entities.UsuarioIncentivo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IUsuarioIncentivoRepository extends JpaRepository<UsuarioIncentivo, Long>{
    Optional<UsuarioIncentivo> findByUsuarioIdAndIncentivoId(Long idUsuario, Long idIncentivo);

    @Query(value = """
        SELECT ui.id, i.id, i.nombre, i.tipo, i.meta_cantidad, i.meta_unidad,
               ui.cantidad_actual, ui.estado, ui.completado_en,
               CASE WHEN ui.estado = 'completado' AND ui.completado_en IS NULL THEN true ELSE false END AS puede_reclamar
        FROM usuario_incentivo ui
        INNER JOIN incentivo i ON ui.id_incentivo = i.id
        WHERE ui.id_usuario = ?1
        ORDER BY i.tipo ASC, i.nombre ASC
        """, nativeQuery = true)
    List<Object[]> progresoIncentivosUsuario(Long idUsuario);

    @Query(value = """
        SELECT ui.id, i.id, i.nombre, i.tipo, i.meta_cantidad,
               ui.cantidad_actual, ui.estado, i.fecha_fin, ui.completado_en,
               CASE WHEN ui.estado = 'completado' AND ui.completado_en IS NULL THEN true ELSE false END AS puede_reclamar,
               CASE WHEN i.fecha_fin IS NOT NULL
                         AND i.fecha_fin BETWEEN CURRENT_DATE AND ?2
                    THEN true ELSE false END AS proximo_a_vencer
        FROM usuario_incentivo ui
        INNER JOIN incentivo i ON ui.id_incentivo = i.id
        WHERE ui.id_usuario = ?1
        AND i.activo = true
        AND (ui.estado IN ('en_progreso', 'completado') OR i.fecha_fin IS NOT NULL)
        ORDER BY proximo_a_vencer DESC, puede_reclamar DESC, i.fecha_fin ASC, i.nombre ASC
        """, nativeQuery = true)
    List<Object[]> recordatoriosIncentivosUsuario(Long idUsuario, LocalDate fechaLimite);
}
