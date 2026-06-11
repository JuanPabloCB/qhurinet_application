package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.qhurinet.entities.TransaccionPuntos;

import java.util.List;

public interface ITransaccionPuntosRepository extends JpaRepository<TransaccionPuntos, Long> {
    @Query(value = """
        SELECT *
        FROM transaccion_puntos
        WHERE id_usuario = ?1
        ORDER BY created_at DESC
        """, nativeQuery = true)
    List<TransaccionPuntos> findByUsuarioIdOrderByCreatedAtDesc(Long idUsuario);

    @Query(value = """
        SELECT *
        FROM transaccion_puntos
        WHERE id_usuario = ?1
          AND LOWER(tipo) = LOWER(?2)
        ORDER BY created_at DESC
        """, nativeQuery = true)
    List<TransaccionPuntos> findByUsuarioIdAndTipoIgnoreCaseOrderByCreatedAtDesc(Long idUsuario, String tipo);

    @Query(value = """
        SELECT COALESCE(SUM(puntos), 0)
        FROM transaccion_puntos
        WHERE id_usuario = ?1
        """, nativeQuery = true)
    Long sumPuntosByUsuarioId(Long idUsuario);

    @Query(value = """
        SELECT COUNT(*)
        FROM transaccion_puntos
        WHERE id_usuario = ?1
          AND referencia_tipo = 'recompensa_diaria'
          AND CAST(created_at AS DATE) = CURRENT_DATE
        """, nativeQuery = true)
    long countRecompensasDiariasHoy(Long idUsuario);
}
