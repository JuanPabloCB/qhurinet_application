package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Recoleccion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IRecoleccionService {
    public List<Recoleccion> list();
    public Recoleccion insert(Recoleccion r);
    public Optional<Recoleccion> listId(Long id);
    public void update(Recoleccion r);
    public void delete(Long id);
    public List<Recoleccion> historialActividades(Long idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado, String material);
}
