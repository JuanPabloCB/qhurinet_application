package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.qhurinet.entities.TransaccionDinero;

import java.util.List;

public interface ITransaccionDineroRepository extends JpaRepository<TransaccionDinero, Long> {
    List<TransaccionDinero> findByUsuarioIdOrderByCreatedAtDesc(Long idUsuario);
}
