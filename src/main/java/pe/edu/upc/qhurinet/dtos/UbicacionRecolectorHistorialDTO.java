package pe.edu.upc.qhurinet.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UbicacionRecolectorHistorialDTO {
    private Long id;
    private Long idRecoleccion;
    private BigDecimal latRecolector;
    private BigDecimal lngRecolector;
    private LocalDateTime createdAt;

    public UbicacionRecolectorHistorialDTO() {
    }

    public UbicacionRecolectorHistorialDTO(Long id, Long idRecoleccion, BigDecimal latRecolector, BigDecimal lngRecolector, LocalDateTime createdAt) {
        this.id = id;
        this.idRecoleccion = idRecoleccion;
        this.latRecolector = latRecolector;
        this.lngRecolector = lngRecolector;
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

    public BigDecimal getLatRecolector() {
        return latRecolector;
    }

    public void setLatRecolector(BigDecimal latRecolector) {
        this.latRecolector = latRecolector;
    }

    public BigDecimal getLngRecolector() {
        return lngRecolector;
    }

    public void setLngRecolector(BigDecimal lngRecolector) {
        this.lngRecolector = lngRecolector;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
