package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Notificacion;

import java.util.List;
import java.util.Optional;

public interface INotificacionService {
    public List<Notificacion> list();
    public Notificacion insert(Notificacion n);
    public Optional<Notificacion> listId(Long id);
    public void update(Notificacion n);
    public void delete(Long id);
    public List<Notificacion> notificacionesPorUsuario(Long idUsuario);
}
