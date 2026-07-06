package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;

public class PublicacionDTO {
    private Long id;
    private Long idUsuario;
    private String usuarioNombre;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String material;
    private String tipoPunto;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Boolean activo;
    private LocalDateTime createdAt;
    private String imagenesJson;

    public PublicacionDTO() {
    }

    public PublicacionDTO(Long id, Long idUsuario, String usuarioNombre, String titulo, String descripcion, String categoria, String material, String tipoPunto, String direccion, Double latitud, Double longitud, Boolean activo, LocalDateTime createdAt, String imagenesJson) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.usuarioNombre = usuarioNombre;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.material = material;
        this.tipoPunto = tipoPunto;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.activo = activo;
        this.createdAt = createdAt;
        this.imagenesJson = imagenesJson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getTipoPunto() {
        return tipoPunto;
    }

    public void setTipoPunto(String tipoPunto) {
        this.tipoPunto = tipoPunto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getImagenesJson() {
        return imagenesJson;
    }

    public void setImagenesJson(String imagenesJson) {
        this.imagenesJson = imagenesJson;
    }
}
