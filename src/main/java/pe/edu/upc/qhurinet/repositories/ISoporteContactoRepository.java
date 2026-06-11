package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.qhurinet.entities.SoporteContacto;

import java.util.List;

public interface ISoporteContactoRepository extends JpaRepository<SoporteContacto, Long> {
    List<SoporteContacto> findByActivoTrueOrderByTipoAsc();
}
