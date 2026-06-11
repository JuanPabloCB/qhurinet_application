package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.UbicacionRecolectorHistorial;
import pe.edu.upc.qhurinet.repositories.IUbicacionRecolectorHistorialRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IUbicacionRecolectorHistorialService;

import java.util.List;

@Service
public class UbicacionRecolectorHistorialServiceImplement implements IUbicacionRecolectorHistorialService {
    @Autowired
    private IUbicacionRecolectorHistorialRepository ubicacionRepository;

    @Override
    public UbicacionRecolectorHistorial insert(UbicacionRecolectorHistorial ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    @Override
    public List<UbicacionRecolectorHistorial> historialPorRecoleccion(Long idRecoleccion) {
        return ubicacionRepository.findByRecoleccionIdOrderByCreatedAtDesc(idRecoleccion);
    }
}
