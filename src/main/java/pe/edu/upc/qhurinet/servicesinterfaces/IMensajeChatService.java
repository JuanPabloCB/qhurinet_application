package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.MensajeChat;

import java.util.List;
import java.util.Optional;

public interface IMensajeChatService {
    public List<MensajeChat> list();
    public MensajeChat insert(MensajeChat m);
    public Optional<MensajeChat> listId(Long id);
    public void update(MensajeChat m);
    public void delete(Long id);
    public List<MensajeChat> mensajesPorUsuario(Long idUsuario);
    public List<MensajeChat> conversacion(Long idUsuarioUno, Long idUsuarioDos);
    public List<MensajeChat> mensajesPorRecoleccion(Long idRecoleccion);
}
