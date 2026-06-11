package pe.edu.upc.qhurinet.servicesimplements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.UsuarioIncentivo;
import pe.edu.upc.qhurinet.repositories.IUsuarioIncentivoRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioIncentivoService;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioIncentivoServiceImplement implements IUsuarioIncentivoService{
    @Autowired
    private IUsuarioIncentivoRepository uR;


    @Override
    public List<UsuarioIncentivo> list() {
        return List.of();
    }

    @Override
    public UsuarioIncentivo insert(UsuarioIncentivo u) {
        return null;
    }

    @Override
    public Optional<UsuarioIncentivo> listId(Long id) {
        return Optional.empty();
    }

    @Override
    public void update(UsuarioIncentivo u) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<UsuarioIncentivo> findByUsuarioIdAndIncentivoId(Long idUsuario, Long idIncentivo) {
        return Optional.empty();
    }

    @Override
    public List<Object[]> progresoIncentivosUsuario(Long idUsuario) {
        return List.of();
    }

    @Override
    public List<Object[]> recordatoriosIncentivosUsuario(Long idUsuario) {
        return List.of();
    }

    @Override
    public UsuarioIncentivo actualizarProgreso(Long id, Integer cantidadActual) {
        return null;
    }

    @Override
    public UsuarioIncentivo reclamar(Long id) {
        return null;
    }
}
