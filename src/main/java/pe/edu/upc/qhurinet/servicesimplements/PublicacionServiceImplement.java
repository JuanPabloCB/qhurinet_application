package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.Publicacion;
import pe.edu.upc.qhurinet.repositories.IPublicacionRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IPublicacionService;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.qhurinet.repositories.IPublicacionMaterialRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PublicacionServiceImplement implements IPublicacionService {
    @Autowired
    private IPublicacionRepository pR;

    @Autowired
    private IPublicacionMaterialRepository pmR;

    @Override
    public List<Publicacion> list() {
        return pR.findAll();
    }

    @Override
    public Publicacion insert(Publicacion p) {
        return pR.save(p);
    }

    @Override
    public Optional<Publicacion> listId(Long id) {
        return pR.findById(id);
    }

    @Override
    public void update(Publicacion p) {
        pR.save(p);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!pR.existsById(id)) {
            throw new IllegalArgumentException("Publicación no encontrada");
        }

        pmR.deleteByPublicacionId(id);
        pR.deleteById(id);
    }

    @Override
    public List<Publicacion> publicacionesPorUsuario(Long idUsuario) {
        return pR.findByUsuarioIdOrderByCreatedAtDesc(idUsuario);
    }

    @Override
    public List<Object[]> buscarPublicacionesPorTexto(String texto) {
        return pR.buscarPublicacionesPorTexto(texto);
    }

    @Override
    public List<Object[]> publicacionesCercanas(Double lat, Double lng, Double radio) {
        return pR.publicacionesCercanas(lat, lng, radio);
    }

    @Override
    public List<Object[]> publicacionesMapa(Double lat, Double lng, Double radioKm, String material, String categoria, String tipoPunto) {
        return pR.publicacionesMapa(lat, lng, radioKm, material, categoria, tipoPunto);
    }

    @Override
    public List<Object[]> publicacionesPorCategoriaMaterial(String categoria) {
        return pR.publicacionesPorCategoriaMaterial(categoria);
    }

    @Override
    public List<Object[]> historialMaterialesUsuario(Long idUsuario) {
        return pR.historialMaterialesUsuario(idUsuario);
    }
}
