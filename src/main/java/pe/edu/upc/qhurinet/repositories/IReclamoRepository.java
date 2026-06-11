package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.qhurinet.entities.Reclamo;

import java.util.List;

public interface IReclamoRepository extends JpaRepository<Reclamo, Long> {
    List<Reclamo> findByUsuarioIdOrderByCreatedAtDesc(Long idUsuario);
}
