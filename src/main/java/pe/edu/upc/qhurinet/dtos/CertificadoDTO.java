package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;

public class CertificadoDTO {
    private Long id;
    private Long idUsuario;
    private String usuarioNombre;
    private String nombre;
    private String descripcion;
    private String nivelDificultad;
    private Integer puntosRequeridos;
    private String urlPdf;
    private LocalDateTime fechaObtencion;

    public CertificadoDTO() {
    }

    public CertificadoDTO(Long id, Long idUsuario, String usuarioNombre, String nombre, String descripcion, String nivelDificultad, Integer puntosRequeridos, String urlPdf, LocalDateTime fechaObtencion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.usuarioNombre = usuarioNombre;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivelDificultad = nivelDificultad;
        this.puntosRequeridos = puntosRequeridos;
        this.urlPdf = urlPdf;
        this.fechaObtencion = fechaObtencion;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public Integer getPuntosRequeridos() {
        return puntosRequeridos;
    }

    public void setPuntosRequeridos(Integer puntosRequeridos) {
        this.puntosRequeridos = puntosRequeridos;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public LocalDateTime getFechaObtencion() {
        return fechaObtencion;
    }

    public void setFechaObtencion(LocalDateTime fechaObtencion) {
        this.fechaObtencion = fechaObtencion;
    }
}
