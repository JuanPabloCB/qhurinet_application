package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.UbicacionRecolectorHistorial;

import java.util.List;

public interface IUbicacionRecolectorHistorialService {
    UbicacionRecolectorHistorial insert(UbicacionRecolectorHistorial ubicacion);
    List<UbicacionRecolectorHistorial> historialPorRecoleccion(Long idRecoleccion);
}
