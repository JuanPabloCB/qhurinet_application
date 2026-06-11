package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Rol;

import java.util.List;
import java.util.Optional;

public interface IRolService {
    public List<Rol> list();
    public Rol insert(Rol r);
    public Optional<Rol> listId(Long id);
    public Optional<Rol> findByNombre(String nombre);
    public void update(Rol r);
    public void delete(Long id);
}
