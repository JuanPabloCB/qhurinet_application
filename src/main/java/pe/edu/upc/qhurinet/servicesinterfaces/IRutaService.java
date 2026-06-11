package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Ruta;

import java.util.List;
import java.util.Optional;

public interface IRutaService {
    public List<Ruta> list();
    public Ruta insert(Ruta r);
    public Optional<Ruta> listId(Long id);
    public void update(Ruta r);
    public void delete(Long id);
    public List<Ruta> rutasPorUsuario(Long idUsuario);
}
