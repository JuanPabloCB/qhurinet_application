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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "publicacion")
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "categoria", length = 80, nullable = false)
    private String categoria;

    @Column(name = "material", length = 80, nullable = false)
    private String material;

    @Column(name = "tipo_punto", length = 50, nullable = false)
    private String tipoPunto;

    @Column(name = "direccion", length = 250)
    private String direccion;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "estado", length = 30)
    private String estado;

    @Column(name = "direccion_referencia", length = 250)
    private String direccionReferencia;

    @Column(name = "fecha_disponibilidad")
    private LocalDate fechaDisponibilidad;

    @Column(name = "imagenes_json", columnDefinition = "TEXT")
    private String imagenesJson;

    public Publicacion() {
    }

    public Publicacion(Long id, Usuario usuario, String titulo, String descripcion, String categoria, String material, String tipoPunto, String direccion, Double latitud, Double longitud, Boolean activo, LocalDateTime createdAt) {
        this.id = id;
        this.usuario = usuario;
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
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.activo == null) {
            this.activo = true;
        }
        if (this.categoria == null) {
            this.categoria = "general";
        }
        if (this.material == null) {
            this.material = "mixto";
        }
        if (this.tipoPunto == null) {
            this.tipoPunto = "recoleccion";
        }
        if (this.estado == null) {
            this.estado = Boolean.TRUE.equals(this.activo) ? "activa" : "inactiva";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
        if (this.descripcion == null) {
            this.descripcion = observaciones;
        }
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

    public String getDireccionReferencia() {
        return direccionReferencia;
    }

    public void setDireccionReferencia(String direccionReferencia) {
        this.direccionReferencia = direccionReferencia;
        if (this.direccion == null) {
            this.direccion = direccionReferencia;
        }
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud == null ? null : latitud.doubleValue();
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud == null ? null : longitud.doubleValue();
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        if (estado != null) {
            this.activo = !"inactiva".equalsIgnoreCase(estado) && !"cancelada".equalsIgnoreCase(estado);
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getFechaDisponibilidad() {
        return fechaDisponibilidad;
    }

    public void setFechaDisponibilidad(LocalDate fechaDisponibilidad) {
        this.fechaDisponibilidad = fechaDisponibilidad;
    }

    public String getImagenesJson() {
        return imagenesJson;
    }

    public void setImagenesJson(String imagenesJson) {
        this.imagenesJson = imagenesJson;
    }
}
