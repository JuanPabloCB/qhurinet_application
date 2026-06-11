package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.TransaccionDinero;

import java.util.List;
import java.util.Optional;

public interface ITransaccionDineroService {
    public List<TransaccionDinero> list();
    public TransaccionDinero insert(TransaccionDinero t);
    public Optional<TransaccionDinero> listId(Long id);
    public void update(TransaccionDinero t);
    public void delete(Long id);
    public List<TransaccionDinero> historialUsuario(Long idUsuario);
}
