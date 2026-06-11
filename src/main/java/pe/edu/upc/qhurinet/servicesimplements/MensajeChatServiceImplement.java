package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.MensajeChat;
import pe.edu.upc.qhurinet.repositories.IMensajeChatRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IMensajeChatService;

import java.util.List;
import java.util.Optional;

@Service
public class MensajeChatServiceImplement implements IMensajeChatService {
    @Autowired
    private IMensajeChatRepository mR;

    @Override
    public List<MensajeChat> list() {
        return mR.findAll();
    }

    @Override
    public MensajeChat insert(MensajeChat m) {
        return mR.save(m);
    }

    @Override
    public Optional<MensajeChat> listId(Long id) {
        return mR.findById(id);
    }

    @Override
    public void update(MensajeChat m) {
        mR.save(m);
    }

    @Override
    public void delete(Long id) {
        mR.deleteById(id);
    }

    @Override
    public List<MensajeChat> mensajesPorUsuario(Long idUsuario) {
        return mR.findByEmisorIdOrReceptorIdOrderByCreatedAtDesc(idUsuario, idUsuario);
    }

    @Override
    public List<MensajeChat> conversacion(Long idUsuarioUno, Long idUsuarioDos) {
        return mR.findByEmisorIdAndReceptorIdOrEmisorIdAndReceptorIdOrderByCreatedAtAsc(idUsuarioUno, idUsuarioDos, idUsuarioDos, idUsuarioUno);
    }

    @Override
    public List<MensajeChat> mensajesPorRecoleccion(Long idRecoleccion) {
        return mR.findByRecoleccionIdOrderByCreatedAtAsc(idRecoleccion);
    }
}
