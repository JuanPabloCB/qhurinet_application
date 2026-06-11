package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Reclamo;

import java.util.List;
import java.util.Optional;

public interface IReclamoService {
    public List<Reclamo> list();
    public Reclamo insert(Reclamo r);
    public Optional<Reclamo> listId(Long id);
    public void update(Reclamo r);
    public void delete(Long id);
    public List<Reclamo> reclamosPorUsuario(Long idUsuario);
}
