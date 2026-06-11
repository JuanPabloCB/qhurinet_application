package pe.edu.upc.qhurinet.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecoleccionDTO {
    private Long id;
    private Long idPublicacion;
    private Long idRecolector;
    private String estado;
    private LocalDateTime fechaProgramada;
    private LocalDateTime fechaCompletada;
    private Boolean prioritaria;
    private String codigoQr;
    private Boolean qrValidado;
    private BigDecimal latRecolector;
    private BigDecimal lngRecolector;
    private LocalDateTime createdAt;
    private String incidenciaDescripcion;
    private String incidenciaEvidenciaUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Long idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public Long getIdRecolector() {
        return idRecolector;
    }

    public void setIdRecolector(Long idRecolector) {
        this.idRecolector = idRecolector;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public LocalDateTime getFechaCompletada() {
        return fechaCompletada;
    }

    public void setFechaCompletada(LocalDateTime fechaCompletada) {
        this.fechaCompletada = fechaCompletada;
    }

    public Boolean getPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(Boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public Boolean getQrValidado() {
        return qrValidado;
    }

    public void setQrValidado(Boolean qrValidado) {
        this.qrValidado = qrValidado;
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

    public String getIncidenciaDescripcion() {
        return incidenciaDescripcion;
    }

    public void setIncidenciaDescripcion(String incidenciaDescripcion) {
        this.incidenciaDescripcion = incidenciaDescripcion;
    }

    public String getIncidenciaEvidenciaUrl() {
        return incidenciaEvidenciaUrl;
    }

    public void setIncidenciaEvidenciaUrl(String incidenciaEvidenciaUrl) {
        this.incidenciaEvidenciaUrl = incidenciaEvidenciaUrl;
    }
}
