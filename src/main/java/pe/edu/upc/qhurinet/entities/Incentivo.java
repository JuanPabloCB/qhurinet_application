package pe.edu.upc.qhurinet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "incentivo")
public class Incentivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "tipo", length = 50, nullable = false)
    private String tipo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "costo_puntos", nullable = false)
    private Integer costoPuntos;

    @Column(name = "meta_cantidad")
    private Integer metaCantidad;

    @Column(name = "meta_unidad", length = 50)
    private String metaUnidad;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    public Incentivo() {
    }

    public Incentivo(Long id, String nombre, String tipo, Integer metaCantidad, String metaUnidad, LocalDate fechaInicio, LocalDate fechaFin, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.metaCantidad = metaCantidad;
        this.metaUnidad = metaUnidad;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
    }

    @PrePersist
    public void prePersist() {
        if (this.activo == null) {
            this.activo = true;
        }
        if (this.costoPuntos == null) {
            this.costoPuntos = 0;
        }
        if (this.stock == null) {
            this.stock = 0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCostoPuntos() {
        return costoPuntos;
    }

    public void setCostoPuntos(Integer costoPuntos) {
        this.costoPuntos = costoPuntos;
    }

    public BigDecimal getCostoPuntosDecimal() {
        return costoPuntos == null ? null : BigDecimal.valueOf(costoPuntos);
    }

    public Integer getMetaCantidad() {
        return metaCantidad;
    }

    public void setMetaCantidad(Integer metaCantidad) {
        this.metaCantidad = metaCantidad;
    }

    public String getMetaUnidad() {
        return metaUnidad;
    }

    public void setMetaUnidad(String metaUnidad) {
        this.metaUnidad = metaUnidad;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
