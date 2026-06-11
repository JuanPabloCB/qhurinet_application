package pe.edu.upc.qhurinet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.qhurinet.entities.Faq;

import java.util.List;

public interface IFaqRepository extends JpaRepository<Faq, Long> {
    List<Faq> findByActivoTrueOrderByCategoriaAscPreguntaAsc();
}
