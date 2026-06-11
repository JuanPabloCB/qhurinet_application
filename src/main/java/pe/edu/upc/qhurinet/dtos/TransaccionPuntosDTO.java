package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;

public class TransaccionPuntosDTO {
    private Long id;
    private Long idUsuario;
    private String tipo;
    private Integer puntos;
    private String motivo;
    private String referenciaTipo;
    private Long referenciaId;
    private LocalDateTime createdAt;

    public TransaccionPuntosDTO() {
    }

    public TransaccionPuntosDTO(Long id, Long idUsuario, String tipo, Integer puntos, String motivo, String referenciaTipo, Long referenciaId, LocalDateTime createdAt) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.puntos = puntos;
        this.motivo = motivo;
        this.referenciaTipo = referenciaTipo;
        this.referenciaId = referenciaId;
        this.createdAt = createdAt;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getReferenciaTipo() {
        return referenciaTipo;
    }

    public void setReferenciaTipo(String referenciaTipo) {
        this.referenciaTipo = referenciaTipo;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
