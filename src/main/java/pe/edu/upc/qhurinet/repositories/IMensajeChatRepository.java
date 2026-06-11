package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.qhurinet.entities.MensajeChat;

import java.util.List;

public interface IMensajeChatRepository extends JpaRepository<MensajeChat, Long> {
    List<MensajeChat> findByEmisorIdOrReceptorIdOrderByCreatedAtDesc(Long idEmisor, Long idReceptor);
    List<MensajeChat> findByEmisorIdAndReceptorIdOrEmisorIdAndReceptorIdOrderByCreatedAtAsc(Long idEmisor, Long idReceptor, Long idReceptorInvertido, Long idEmisorInvertido);
    List<MensajeChat> findByRecoleccionIdOrderByCreatedAtAsc(Long idRecoleccion);
}
