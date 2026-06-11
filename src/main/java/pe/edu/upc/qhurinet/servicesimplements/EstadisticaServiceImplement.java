package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.repositories.ICalificacionRepository;
import pe.edu.upc.qhurinet.repositories.IPublicacionMaterialRepository;
import pe.edu.upc.qhurinet.repositories.IPublicacionRepository;
import pe.edu.upc.qhurinet.repositories.IRecoleccionRepository;
import pe.edu.upc.qhurinet.repositories.ITransaccionPuntosRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IEstadisticaService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstadisticaServiceImplement implements IEstadisticaService {
    @Autowired
    private IPublicacionRepository pR;

    @Autowired
    private IRecoleccionRepository rR;

    @Autowired
    private IPublicacionMaterialRepository pMR;

    @Autowired
    private ITransaccionPuntosRepository tR;

    @Autowired
    private ICalificacionRepository cR;

    @Override
    public Map<String, Object> globales() {
        return Map.of(
                "publicaciones", pR.count(),
                "recolecciones", rR.count(),
                "materialesRegistrados", pMR.count(),
                "puntosTransacciones", tR.count(),
                "calificaciones", cR.count()
        );
    }

    @Override
    public List<Map<String, Object>> materialesReciclados() {
        return pMR.cantidadKgPorCategoria().stream()
                .map(row -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("categoria", row[0]);
                    map.put("cantidadKg", row[1]);
                    return map;
                })
                .toList();
    }

    @Override
    public Map<String, Object> reporteUsuario(Long idUsuario) {
        long publicaciones = pR.countByUsuarioId(idUsuario);
        long recolecciones = rR.countActividadesUsuario(idUsuario);
        Long puntos = tR.sumPuntosByUsuarioId(idUsuario);
        return Map.of("idUsuario", idUsuario, "publicaciones", publicaciones, "recolecciones", recolecciones, "puntosMovidos", puntos == null ? 0 : puntos);
    }
}
