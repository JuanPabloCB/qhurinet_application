package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Calificacion;

import java.util.List;
import java.util.Optional;

public interface ICalificacionService {
    public List<Calificacion> list();
    public Calificacion insert(Calificacion c);
    public Optional<Calificacion> listId(Long id);
    public void update(Calificacion c);
    public void delete(Long id);
    public List<Calificacion> calificacionesPorRecolector(Long idRecolector);
    public List<Calificacion> calificacionesPorRecoleccion(Long idRecoleccion);
    public boolean existeCalificacionPorRecoleccionYAutor(Long idRecoleccion, Long idAutor);
}
