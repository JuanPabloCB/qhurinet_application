package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.Notificacion;
import pe.edu.upc.qhurinet.repositories.INotificacionRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.INotificacionService;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionServiceImplement implements INotificacionService {
    @Autowired
    private INotificacionRepository nR;

    @Override
    public List<Notificacion> list() {
        return nR.findAll();
    }

    @Override
    public Notificacion insert(Notificacion n) {
        return nR.save(n);
    }

    @Override
    public Optional<Notificacion> listId(Long id) {
        return nR.findById(id);
    }

    @Override
    public void update(Notificacion n) {
        nR.save(n);
    }

    @Override
    public void delete(Long id) {
        nR.deleteById(id);
    }

    @Override
    public List<Notificacion> notificacionesPorUsuario(Long idUsuario) {
        return nR.findByUsuarioIdOrderByCreatedAtDesc(idUsuario);
    }
}
