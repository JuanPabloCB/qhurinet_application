package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.MetodoPago;

import java.util.List;
import java.util.Optional;

public interface IMetodoPagoService {
    public List<MetodoPago> list();
    public MetodoPago insert(MetodoPago m);
    public Optional<MetodoPago> listId(Long id);
    public void update(MetodoPago m);
    public void delete(Long id);
    public List<MetodoPago> metodosPorUsuario(Long idUsuario);
}
