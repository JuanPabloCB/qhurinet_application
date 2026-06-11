package pe.edu.upc.qhurinet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.UsuarioDTO;
import pe.edu.upc.qhurinet.dtos.UsuarioRequestDTO;
import pe.edu.upc.qhurinet.entities.Rol;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private IUsuarioService uS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDTO> list() {
        return uS.list().stream().map(this::toDTO).toList();
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public UsuarioDTO me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return toDTO(uS.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioDTO listId(@PathVariable Long id) {
        return toDTO(uS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")));
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioDTO insert(@RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setEnabled(dto.getEnabled());
        return toDTO(uS.guardarConRoles(usuario, dto.getRoles()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioDTO update(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = uS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setUsername(dto.getUsername());
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setEnabled(dto.getEnabled());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(dto.getPassword());
        }
        return toDTO(uS.guardarConRoles(usuario, dto.getRoles()));
    }

    @GetMapping("/{id}/perfil")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public java.util.Map<String, Object> perfil(@PathVariable Long id) {
        Usuario usuario = uS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return java.util.Map.of(
                "id", usuario.getId(),
                "username", usuario.getUsername(),
                "nombre", usuario.getNombre(),
                "tipoCuenta", usuario.getTipoCuenta() == null ? "" : usuario.getTipoCuenta(),
                "descripcion", usuario.getDescripcion() == null ? "" : usuario.getDescripcion(),
                "fotoUrl", usuario.getFotoUrl() == null ? "" : usuario.getFotoUrl(),
                "verificado", Boolean.TRUE.equals(usuario.getVerificado()),
                "disponible", Boolean.TRUE.equals(usuario.getDisponible()),
                "puntosTotales", usuario.getPuntosTotales() == null ? 0 : usuario.getPuntosTotales(),
                "nivelParticipacion", usuario.getNivelParticipacion() == null ? "" : usuario.getNivelParticipacion()
        );
    }

    @PutMapping("/{id}/nivel")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public java.util.Map<String, Object> recalcularNivel(@PathVariable Long id) {
        securityUserResolver.requireAdminOrUser(id);
        Usuario usuario = uS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        String nivel = calcularNivel(usuario.getPuntosTotales());
        usuario.setNivelParticipacion(nivel);
        uS.update(usuario);
        return java.util.Map.of(
                "idUsuario", usuario.getId(),
                "puntosTotales", usuario.getPuntosTotales() == null ? 0 : usuario.getPuntosTotales(),
                "nivelParticipacion", nivel,
                "siguienteNivel", siguienteNivel(usuario.getPuntosTotales()),
                "puntosParaSiguienteNivel", puntosParaSiguienteNivel(usuario.getPuntosTotales())
        );
    }

    @PutMapping("/{id}/datos-basicos")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public UsuarioDTO completarDatosBasicos(@PathVariable Long id, @RequestBody java.util.Map<String, Object> request) {
        securityUserResolver.requireAdminOrUser(id);
        Usuario usuario = uS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (request.get("nombre") != null) {
            usuario.setNombre(request.get("nombre").toString());
        }
        if (request.get("telefono") != null) {
            usuario.setTelefono(request.get("telefono").toString());
        }
        uS.update(usuario);
        return toDTO(usuario);
    }

    @PutMapping("/{id}/disponibilidad")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public UsuarioDTO definirDisponibilidad(@PathVariable Long id, @RequestBody java.util.Map<String, Object> request) {
        securityUserResolver.requireAdminOrUser(id);
        Usuario usuario = uS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setDisponible(Boolean.parseBoolean(request.get("disponible").toString()));
        uS.update(usuario);
        return toDTO(usuario);
    }

    @PutMapping("/{id}/foto")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public UsuarioDTO establecerFoto(@PathVariable Long id, @RequestBody java.util.Map<String, Object> request) {
        securityUserResolver.requireAdminOrUser(id);
        Usuario usuario = uS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setFotoUrl(request.get("fotoUrl").toString());
        uS.update(usuario);
        return toDTO(usuario);
    }

    @PutMapping("/{id}/tipo-cuenta")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public UsuarioDTO seleccionarTipoCuenta(@PathVariable Long id, @RequestBody java.util.Map<String, Object> request) {
        securityUserResolver.requireAdminOrUser(id);
        Usuario usuario = uS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setTipoCuenta(request.get("tipoCuenta").toString());
        uS.update(usuario);
        return toDTO(usuario);
    }

    @PutMapping("/{id}/descripcion")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public UsuarioDTO actualizarDescripcion(@PathVariable Long id, @RequestBody java.util.Map<String, Object> request) {
        securityUserResolver.requireAdminOrUser(id);
        Usuario usuario = uS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setDescripcion(request.get("descripcion").toString());
        uS.update(usuario);
        return toDTO(usuario);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        uS.delete(id);
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        ModelMapper m = new ModelMapper();
        UsuarioDTO dto = m.map(usuario, UsuarioDTO.class);
        Set<String> roles = usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet());
        dto.setRoles(roles);
        return dto;
    }

    private String calcularNivel(Integer puntosTotales) {
        int puntos = puntosTotales == null ? 0 : puntosTotales;
        if (puntos >= 1000) {
            return "PLATINO";
        }
        if (puntos >= 500) {
            return "ORO";
        }
        if (puntos >= 100) {
            return "PLATA";
        }
        return "BRONCE";
    }

    private String siguienteNivel(Integer puntosTotales) {
        int puntos = puntosTotales == null ? 0 : puntosTotales;
        if (puntos < 100) {
            return "PLATA";
        }
        if (puntos < 500) {
            return "ORO";
        }
        if (puntos < 1000) {
            return "PLATINO";
        }
        return "MAXIMO";
    }

    private int puntosParaSiguienteNivel(Integer puntosTotales) {
        int puntos = puntosTotales == null ? 0 : puntosTotales;
        if (puntos < 100) {
            return 100 - puntos;
        }
        if (puntos < 500) {
            return 500 - puntos;
        }
        if (puntos < 1000) {
            return 1000 - puntos;
        }
        return 0;
    }

}
