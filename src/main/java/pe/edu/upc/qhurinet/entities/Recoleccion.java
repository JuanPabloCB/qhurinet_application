package pe.edu.upc.qhurinet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recoleccion")
public class Recoleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_publicacion", nullable = false)
    private Publicacion publicacion;

    @ManyToOne
    @JoinColumn(name = "id_recolector", nullable = false)
    private Usuario recolector;

    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    @Column(name = "fecha_programada")
    private LocalDateTime fechaProgramada;

    @Column(name = "fecha_completada")
    private LocalDateTime fechaCompletada;

    @Column(name = "prioritaria", nullable = false)
    private Boolean prioritaria;

    @Column(name = "codigo_qr", length = 120)
    private String codigoQr;

    @Column(name = "qr_validado", nullable = false)
    private Boolean qrValidado;

    @Column(name = "lat_recolector", precision = 12, scale = 8)
    private BigDecimal latRecolector;

    @Column(name = "lng_recolector", precision = 12, scale = 8)
    private BigDecimal lngRecolector;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "incidencia_descripcion", columnDefinition = "TEXT")
    private String incidenciaDescripcion;

    @Column(name = "incidencia_evidencia_url", length = 500)
    private String incidenciaEvidenciaUrl;

    public Recoleccion() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "programada";
        }
        if (this.prioritaria == null) {
            this.prioritaria = false;
        }
        if (this.qrValidado == null) {
            this.qrValidado = false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public Usuario getRecolector() {
        return recolector;
    }

    public void setRecolector(Usuario recolector) {
        this.recolector = recolector;
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
