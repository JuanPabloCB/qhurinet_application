package pe.edu.upc.qhurinet.dtos;

public class RegisterRequestDTO {
    private String username;
    private String password;
    private String nombre;
    private String correo;
    private String tipoCuenta;

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(String username, String password, String nombre, String correo) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.correo = correo;
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

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
}
