package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.qhurinet.entities.Incentivo;

import java.util.List;

public interface IIncentivoRepository extends JpaRepository<Incentivo, Long> {
    @Query(value = """
        SELECT *
        FROM incentivo
        WHERE activo = true
        ORDER BY nombre ASC
        """, nativeQuery = true)
    List<Incentivo> findByActivoTrueOrderByNombreAsc();

    @Query(value = """
        SELECT *
        FROM incentivo
        WHERE activo = true
          AND LOWER(tipo) = LOWER(?1)
        ORDER BY nombre ASC
        """, nativeQuery = true)
    List<Incentivo> findByActivoTrueAndTipoIgnoreCaseOrderByNombreAsc(String tipo);
}
