package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Incentivo;

import java.util.List;
import java.util.Optional;

public interface IIncentivoService {
    public List<Incentivo> list();
    public Incentivo insert(Incentivo i);
    public Optional<Incentivo> listId(Long id);
    public void update(Incentivo i);
    public void delete(Long id);
    public List<Incentivo> incentivosActivos();
    public List<Incentivo> incentivosActivosPorTipo(String tipo);
}
