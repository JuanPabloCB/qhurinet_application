package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.qhurinet.entities.UbicacionRecolectorHistorial;

import java.util.List;

public interface IUbicacionRecolectorHistorialRepository extends JpaRepository<UbicacionRecolectorHistorial, Long> {
    List<UbicacionRecolectorHistorial> findByRecoleccionIdOrderByCreatedAtDesc(Long idRecoleccion);
}
