package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.UsuarioIncentivo;
import pe.edu.upc.qhurinet.repositories.IUsuarioIncentivoRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioIncentivoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioIncentivoServiceImplement implements IUsuarioIncentivoService {
    @Autowired
    private IUsuarioIncentivoRepository uR;

    @Override
    public List<UsuarioIncentivo> list() {
        return uR.findAll();
    }

    @Override
    public UsuarioIncentivo insert(UsuarioIncentivo u) {
        return uR.save(u);
    }

    @Override
    public Optional<UsuarioIncentivo> listId(Long id) {
        return uR.findById(id);
    }

    @Override
    public void update(UsuarioIncentivo u) {
        uR.save(u);
    }

    @Override
    public void delete(Long id) {
        uR.deleteById(id);
    }

    @Override
    public Optional<UsuarioIncentivo> findByUsuarioIdAndIncentivoId(Long idUsuario, Long idIncentivo) {
        return uR.findByUsuarioIdAndIncentivoId(idUsuario, idIncentivo);
    }

    @Override
    public List<Object[]> progresoIncentivosUsuario(Long idUsuario) {
        return uR.progresoIncentivosUsuario(idUsuario);
    }

    @Override
    public List<Object[]> recordatoriosIncentivosUsuario(Long idUsuario) {
        return uR.recordatoriosIncentivosUsuario(idUsuario, LocalDate.now().plusDays(7));
    }

    @Override
    public UsuarioIncentivo actualizarProgreso(Long id, Integer cantidadActual) {
        UsuarioIncentivo usuarioIncentivo = uR.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario incentivo no encontrado"));
        usuarioIncentivo.setCantidadActual(cantidadActual);
        if (cantidadActual != null
                && usuarioIncentivo.getIncentivo() != null
                && usuarioIncentivo.getIncentivo().getMetaCantidad() != null
                && cantidadActual >= usuarioIncentivo.getIncentivo().getMetaCantidad()) {
            usuarioIncentivo.setEstado("completado");
        }
        return uR.save(usuarioIncentivo);
    }

    @Override
    public UsuarioIncentivo reclamar(Long id) {
        UsuarioIncentivo usuarioIncentivo = uR.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario incentivo no encontrado"));
        if (!"completado".equals(usuarioIncentivo.getEstado())) {
            throw new IllegalArgumentException("El incentivo aun no esta completado");
        }
        usuarioIncentivo.setCompletadoEn(LocalDateTime.now());
        usuarioIncentivo.setEstado("reclamado");
        return uR.save(usuarioIncentivo);
    }
}
