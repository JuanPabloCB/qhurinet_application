package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.Rol;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.repositories.IRolRepository;
import pe.edu.upc.qhurinet.repositories.IUsuarioRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioServiceImplement implements IUsuarioService {
    @Autowired
    private IUsuarioRepository uR;

    @Autowired
    private IRolRepository rR;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> list() {
        return uR.findAll();
    }

    @Override
    public Usuario insert(Usuario u) {
        return uR.save(u);
    }

    @Override
    public Usuario registrarUsuario(Usuario u) {
        if (uR.existsByUsername(u.getUsername())) {
            throw new IllegalArgumentException("El username ya existe");
        }
        if (uR.existsByCorreo(u.getCorreo())) {
            throw new IllegalArgumentException("El correo ya existe");
        }
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        String tipoCuenta = normalizarTipoCuenta(u.getTipoCuenta());
        u.setTipoCuenta(tipoCuenta);
        Set<Rol> roles = new HashSet<>();
        roles.add(obtenerOCrearRol("USER"));
        roles.add(obtenerOCrearRol(tipoCuenta));
        u.setRoles(roles);
        return uR.save(u);
    }

    @Override
    public Usuario guardarConRoles(Usuario u, Set<String> roles) {
        if (u.getId() == null && uR.existsByUsername(u.getUsername())) {
            throw new IllegalArgumentException("El username ya existe");
        }
        if (u.getId() == null && uR.existsByCorreo(u.getCorreo())) {
            throw new IllegalArgumentException("El correo ya existe");
        }
        if (u.getPassword() != null && !u.getPassword().isBlank() && !u.getPassword().startsWith("$2")) {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        }
        Set<Rol> rolesEntity = new HashSet<>();
        if (roles == null || roles.isEmpty()) {
            rolesEntity.add(rR.findByNombre("USER").orElseGet(() -> rR.save(new Rol(null, "USER"))));
        } else {
            for (String nombreRol : roles) {
                String normalizado = nombreRol.replace("ROLE_", "").toUpperCase();
                rolesEntity.add(rR.findByNombre(normalizado).orElseGet(() -> rR.save(new Rol(null, normalizado))));
            }
        }
        u.setRoles(rolesEntity);
        return uR.save(u);
    }

    @Override
    public Optional<Usuario> listId(Long id) {
        return uR.findById(id);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return uR.findByUsername(username);
    }

    @Override
    public void update(Usuario u) {
        uR.save(u);
    }

    @Override
    public void delete(Long id) {
        uR.deleteById(id);
    }

    private Rol obtenerOCrearRol(String nombre) {
        String normalizado = nombre == null ? "USER" : nombre.replace("ROLE_", "").trim().toUpperCase();
        return rR.findByNombre(normalizado).orElseGet(() -> rR.save(new Rol(null, normalizado)));
    }

    private String normalizarTipoCuenta(String tipoCuenta) {
        String normalizado = tipoCuenta == null ? "GENERADOR" : tipoCuenta.replace("ROLE_", "").trim().toUpperCase();

        return switch (normalizado) {
            case "EMISOR", "GENERADOR", "USER" -> "GENERADOR";
            case "RECOLECTOR" -> "RECOLECTOR";
            case "BODEGA" -> "BODEGA";
            default -> "GENERADOR";
        };
    }
}
