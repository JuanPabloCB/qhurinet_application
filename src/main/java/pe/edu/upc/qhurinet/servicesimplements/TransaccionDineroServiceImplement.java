package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.TransaccionDinero;
import pe.edu.upc.qhurinet.repositories.ITransaccionDineroRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.ITransaccionDineroService;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionDineroServiceImplement implements ITransaccionDineroService {
    @Autowired
    private ITransaccionDineroRepository tR;

    @Override
    public List<TransaccionDinero> list() {
        return tR.findAll();
    }

    @Override
    public TransaccionDinero insert(TransaccionDinero t) {
        return tR.save(t);
    }

    @Override
    public Optional<TransaccionDinero> listId(Long id) {
        return tR.findById(id);
    }

    @Override
    public void update(TransaccionDinero t) {
        tR.save(t);
    }

    @Override
    public void delete(Long id) {
        tR.deleteById(id);
    }

    @Override
    public List<TransaccionDinero> historialUsuario(Long idUsuario) {
        return tR.findByUsuarioIdOrderByCreatedAtDesc(idUsuario);
    }
}
