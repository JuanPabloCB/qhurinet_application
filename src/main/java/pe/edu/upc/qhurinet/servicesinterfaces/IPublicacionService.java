package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Publicacion;

import java.util.List;
import java.util.Optional;

public interface IPublicacionService {
    public List<Publicacion> list();
    public Publicacion insert(Publicacion p);
    public Optional<Publicacion> listId(Long id);
    public void update(Publicacion p);
    public void delete(Long id);
    public List<Publicacion> publicacionesPorUsuario(Long idUsuario);
    public List<Object[]> buscarPublicacionesPorTexto(String texto);
    public List<Object[]> publicacionesCercanas(Double lat, Double lng, Double radio);
    public List<Object[]> publicacionesMapa(Double lat, Double lng, Double radioKm, String material, String categoria, String tipoPunto);
    public List<Object[]> publicacionesPorCategoriaMaterial(String categoria);
    public List<Object[]> historialMaterialesUsuario(Long idUsuario);
}
