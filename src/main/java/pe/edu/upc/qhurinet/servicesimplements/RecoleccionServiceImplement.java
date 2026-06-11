package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.Recoleccion;
import pe.edu.upc.qhurinet.repositories.IRecoleccionRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IRecoleccionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecoleccionServiceImplement implements IRecoleccionService {
    @Autowired
    private IRecoleccionRepository rR;

    @Override
    public List<Recoleccion> list() {
        return rR.findAll();
    }

    @Override
    public Recoleccion insert(Recoleccion r) {
        return rR.save(r);
    }

    @Override
    public Optional<Recoleccion> listId(Long id) {
        return rR.findById(id);
    }

    @Override
    public void update(Recoleccion r) {
        rR.save(r);
    }

    @Override
    public void delete(Long id) {
        rR.deleteById(id);
    }

    @Override
    public List<Recoleccion> historialActividades(Long idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado, String material) {
        return rR.historialActividades(idUsuario, fechaInicio, fechaFin, estado, material);
    }
}
