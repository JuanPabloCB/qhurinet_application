package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.TransaccionPuntos;

import java.util.List;
import java.util.Optional;

public interface ITransaccionPuntosService {
    public List<TransaccionPuntos> list();
    public TransaccionPuntos insert(TransaccionPuntos t);
    public Optional<TransaccionPuntos> listId(Long id);
    public void update(TransaccionPuntos t);
    public void delete(Long id);
    public List<TransaccionPuntos> historialUsuario(Long idUsuario);
    public List<TransaccionPuntos> historialUsuarioPorTipo(Long idUsuario, String tipo);
    public boolean recibioRecompensaDiariaHoy(Long idUsuario);
}
