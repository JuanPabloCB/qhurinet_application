package pe.edu.upc.qhurinet.dtos;

import java.util.Set;

public class UsuarioRequestDTO {
    private String username;
    private String password;
    private String nombre;
    private String correo;
    private Boolean enabled;
    private Set<String> roles;

    public UsuarioRequestDTO() {
    }

    public UsuarioRequestDTO(String username, String password, String nombre, String correo, Boolean enabled, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.correo = correo;
        this.enabled = enabled;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
