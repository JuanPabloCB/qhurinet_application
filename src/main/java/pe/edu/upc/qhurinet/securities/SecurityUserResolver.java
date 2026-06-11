package pe.edu.upc.qhurinet.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.Arrays;

@Component
public class SecurityUserResolver {
    @Autowired
    private IUsuarioService usuarioService;

    public Usuario currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AccessDeniedException("Usuario no autenticado");
        }
        return usuarioService.findByUsername(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("Usuario autenticado no encontrado"));
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    public void requireAdminOrUser(Long idUsuario) {
        if (isAdmin()) {
            return;
        }
        if (idUsuario == null || !idUsuario.equals(currentUser().getId())) {
            throw new AccessDeniedException("No tienes permisos para acceder a este recurso");
        }
    }

    public void requireAdminOrAnyUser(Long... idsUsuario) {
        if (isAdmin()) {
            return;
        }
        Long currentUserId = currentUser().getId();
        boolean allowed = Arrays.stream(idsUsuario)
                .anyMatch(id -> id != null && id.equals(currentUserId));
        if (!allowed) {
            throw new AccessDeniedException("No tienes permisos para acceder a este recurso");
        }
    }
}
