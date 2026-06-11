package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.qhurinet.entities.Material;

import java.util.List;

public interface IMaterialRepository extends JpaRepository<Material, Long> {
    @Query(value = """
        SELECT *
        FROM material
        WHERE LOWER(nombre) LIKE LOWER(CONCAT('%', ?1, '%'))
           OR LOWER(categoria) LIKE LOWER(CONCAT('%', ?1, '%'))
           OR LOWER(COALESCE(descripcion, '')) LIKE LOWER(CONCAT('%', ?1, '%'))
        ORDER BY categoria ASC, nombre ASC
        """, nativeQuery = true)
    List<Material> buscarPorTexto(String texto);
}
