package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IUsuarioService {
    public List<Usuario> list();
    public Usuario insert(Usuario u);
    public Usuario registrarUsuario(Usuario u);
    public Usuario guardarConRoles(Usuario u, Set<String> roles);
    public Optional<Usuario> listId(Long id);
    public Optional<Usuario> findByUsername(String username);
    public void update(Usuario u);
    public void delete(Long id);
}
