package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.qhurinet.entities.PublicacionMaterial;
import pe.edu.upc.qhurinet.entities.PublicacionMaterialId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IPublicacionMaterialRepository extends JpaRepository<PublicacionMaterial, PublicacionMaterialId> {
    List<PublicacionMaterial> findByPublicacion_Id(Long idPublicacion);

    @Modifying
    @Transactional
    @Query("DELETE FROM PublicacionMaterial pm WHERE pm.publicacion.id = :idPublicacion")
    void deleteByPublicacionId(@Param("idPublicacion") Long idPublicacion);

    @Query(value = """
        SELECT m.categoria, SUM(pm.cantidad)
        FROM publicacion_material pm
        INNER JOIN material m ON pm.id_material = m.id
        GROUP BY m.categoria
        ORDER BY m.categoria ASC
        """, nativeQuery = true)
    List<Object[]> cantidadKgPorCategoria();
}
