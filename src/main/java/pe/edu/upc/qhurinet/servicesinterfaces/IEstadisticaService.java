package pe.edu.upc.qhurinet.servicesinterfaces;

import java.util.List;
import java.util.Map;

public interface IEstadisticaService {
    public Map<String, Object> globales();
    public List<Map<String, Object>> materialesReciclados();
    public Map<String, Object> reporteUsuario(Long idUsuario);
}
