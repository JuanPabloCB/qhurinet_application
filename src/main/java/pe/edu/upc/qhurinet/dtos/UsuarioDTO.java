package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;
import java.util.Set;

public class UsuarioDTO {
    private Long id;
    private String username;
    private String nombre;
    private String correo;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private Set<String> roles;
    private String tipoCuenta;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String username, String nombre, String correo, Boolean enabled, LocalDateTime createdAt, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.correo = correo;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
}
