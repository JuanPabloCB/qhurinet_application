package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.TransaccionPuntos;
import pe.edu.upc.qhurinet.repositories.ITransaccionPuntosRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.ITransaccionPuntosService;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionPuntosServiceImplement implements ITransaccionPuntosService {
    @Autowired
    private ITransaccionPuntosRepository tR;

    @Override
    public List<TransaccionPuntos> list() {
        return tR.findAll();
    }

    @Override
    public TransaccionPuntos insert(TransaccionPuntos t) {
        return tR.save(t);
    }

    @Override
    public Optional<TransaccionPuntos> listId(Long id) {
        return tR.findById(id);
    }

    @Override
    public void update(TransaccionPuntos t) {
        tR.save(t);
    }

    @Override
    public void delete(Long id) {
        tR.deleteById(id);
    }

    @Override
    public List<TransaccionPuntos> historialUsuario(Long idUsuario) {
        return tR.findByUsuarioIdOrderByCreatedAtDesc(idUsuario);
    }

    @Override
    public List<TransaccionPuntos> historialUsuarioPorTipo(Long idUsuario, String tipo) {
        return tR.findByUsuarioIdAndTipoIgnoreCaseOrderByCreatedAtDesc(idUsuario, tipo);
    }

    @Override
    public boolean recibioRecompensaDiariaHoy(Long idUsuario) {
        return tR.countRecompensasDiariasHoy(idUsuario) > 0;
    }
}
