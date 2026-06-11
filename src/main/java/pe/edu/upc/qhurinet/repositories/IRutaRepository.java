package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.qhurinet.entities.Ruta;

import java.util.List;

public interface IRutaRepository extends JpaRepository<Ruta, Long> {
    List<Ruta> findByUsuarioIdOrderByCreatedAtDesc(Long idUsuario);
}
