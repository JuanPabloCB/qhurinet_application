package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;

public class CalificacionDTO {
    private Long id;
    private Long idRecoleccion;
    private Long idAutor;
    private Integer puntuacion;
    private String comentario;
    private LocalDateTime createdAt;

    public CalificacionDTO() {
    }

    public CalificacionDTO(Long id, Long idRecoleccion, Long idAutor, Integer puntuacion, String comentario, LocalDateTime createdAt) {
        this.id = id;
        this.idRecoleccion = idRecoleccion;
        this.idAutor = idAutor;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdRecoleccion() {
        return idRecoleccion;
    }

    public void setIdRecoleccion(Long idRecoleccion) {
        this.idRecoleccion = idRecoleccion;
    }

    public Long getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Long idAutor) {
        this.idAutor = idAutor;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
