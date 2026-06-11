package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;

public class MensajeChatDTO {
    private Long id;
    private Long idEmisor;
    private String emisorNombre;
    private Long idReceptor;
    private String receptorNombre;
    private Long idRecoleccion;
    private String contenido;
    private Boolean leido;
    private LocalDateTime createdAt;

    public MensajeChatDTO() {
    }

    public MensajeChatDTO(Long id, Long idEmisor, String emisorNombre, Long idReceptor, String receptorNombre, Long idRecoleccion, String contenido, Boolean leido, LocalDateTime createdAt) {
        this.id = id;
        this.idEmisor = idEmisor;
        this.emisorNombre = emisorNombre;
        this.idReceptor = idReceptor;
        this.receptorNombre = receptorNombre;
        this.idRecoleccion = idRecoleccion;
        this.contenido = contenido;
        this.leido = leido;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(Long idEmisor) {
        this.idEmisor = idEmisor;
    }

    public String getEmisorNombre() {
        return emisorNombre;
    }

    public void setEmisorNombre(String emisorNombre) {
        this.emisorNombre = emisorNombre;
    }

    public Long getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(Long idReceptor) {
        this.idReceptor = idReceptor;
    }

    public String getReceptorNombre() {
        return receptorNombre;
    }

    public void setReceptorNombre(String receptorNombre) {
        this.receptorNombre = receptorNombre;
    }

    public Long getIdRecoleccion() {
        return idRecoleccion;
    }

    public void setIdRecoleccion(Long idRecoleccion) {
        this.idRecoleccion = idRecoleccion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
