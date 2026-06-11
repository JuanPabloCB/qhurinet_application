package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.Rol;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.repositories.IUsuarioRepository;

import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private IUsuarioRepository uR;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = uR.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        List<SimpleGrantedAuthority> authorities = usuario.getRoles()
                .stream()
                .map(Rol::getNombre)
                .map(this::toAuthority)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                Boolean.TRUE.equals(usuario.getEnabled()),
                true,
                true,
                true,
                authorities
        );
    }

    private String toAuthority(String rol) {
        String normalizado = rol == null ? "USER" : rol.toUpperCase();
        if (normalizado.startsWith("ROLE_")) {
            return normalizado;
        }
        return "ROLE_" + normalizado;
    }
}
