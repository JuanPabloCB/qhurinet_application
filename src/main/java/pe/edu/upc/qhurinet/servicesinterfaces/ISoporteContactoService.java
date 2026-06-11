package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.SoporteContacto;

import java.util.List;
import java.util.Optional;

public interface ISoporteContactoService {
    public List<SoporteContacto> list();
    public SoporteContacto insert(SoporteContacto s);
    public Optional<SoporteContacto> listId(Long id);
    public void update(SoporteContacto s);
    public void delete(Long id);
    public List<SoporteContacto> activos();
}
