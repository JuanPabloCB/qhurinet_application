package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.qhurinet.entities.MetodoPago;

import java.util.List;

public interface IMetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
    List<MetodoPago> findByUsuarioIdOrderByPrincipalDescCreatedAtDesc(Long idUsuario);
}
