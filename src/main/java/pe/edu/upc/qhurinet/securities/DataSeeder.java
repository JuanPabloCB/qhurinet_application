package pe.edu.upc.qhurinet.securities;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upc.qhurinet.entities.Rol;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.repositories.IRolRepository;
import pe.edu.upc.qhurinet.repositories.IUsuarioRepository;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataSeeder {
    @Bean
    public CommandLineRunner seedSecurityData(IRolRepository rolRepository, IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Rol rolUser = rolRepository.findByNombre("USER").orElseGet(() -> rolRepository.save(new Rol(null, "USER")));
            Rol rolAdmin = rolRepository.findByNombre("ADMIN").orElseGet(() -> rolRepository.save(new Rol(null, "ADMIN")));

            if (!usuarioRepository.existsByUsername("admin")) {
                Set<Rol> roles = new HashSet<>();
                roles.add(rolUser);
                roles.add(rolAdmin);

                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setNombre("Administrador");
                admin.setCorreo("admin@qhurinet.local");
                admin.setEnabled(true);
                admin.setRoles(roles);
                usuarioRepository.save(admin);
            }
        };
    }
}
