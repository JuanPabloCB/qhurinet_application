package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.UsuarioIncentivo;

import java.util.List;
import java.util.Optional;

public interface IUsuarioIncentivoService {
    public List<UsuarioIncentivo> list();
    public UsuarioIncentivo insert(UsuarioIncentivo u);
    public Optional<UsuarioIncentivo> listId(Long id);
    public void update(UsuarioIncentivo u);
    public void delete(Long id);
    public Optional<UsuarioIncentivo> findByUsuarioIdAndIncentivoId(Long idUsuario, Long idIncentivo);
    public List<Object[]> progresoIncentivosUsuario(Long idUsuario);
    public List<Object[]> recordatoriosIncentivosUsuario(Long idUsuario);
    public UsuarioIncentivo actualizarProgreso(Long id, Integer cantidadActual);
    public UsuarioIncentivo reclamar(Long id);
}
