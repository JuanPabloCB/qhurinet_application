package pe.edu.upc.qhurinet.dtos;
import java.time.LocalDate;

public class IncentivoDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private Integer metaCantidad;
    private String metaUnidad;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;

    public IncentivoDTO() {
    }

    public IncentivoDTO(Long id, String nombre, String tipo, Integer metaCantidad, String metaUnidad, LocalDate fechaInicio, LocalDate fechaFin, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.metaCantidad = metaCantidad;
        this.metaUnidad = metaUnidad;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
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
