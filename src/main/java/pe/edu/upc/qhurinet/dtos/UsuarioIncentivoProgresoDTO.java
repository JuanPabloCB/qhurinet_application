package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;

public class UsuarioIncentivoProgresoDTO {
    private Long idUsuarioIncentivo;
    private Long idIncentivo;
    private String nombre;
    private String tipo;
    private Integer metaCantidad;
    private String metaUnidad;
    private Integer cantidadActual;
    private String estado;
    private LocalDateTime completadoEn;
    private Boolean puedeReclamar;

    public UsuarioIncentivoProgresoDTO() {
    }

    public UsuarioIncentivoProgresoDTO(Long idUsuarioIncentivo, Long idIncentivo, String nombre, String tipo, Integer metaCantidad, String metaUnidad, Integer cantidadActual, String estado, LocalDateTime completadoEn, Boolean puedeReclamar) {
        this.idUsuarioIncentivo = idUsuarioIncentivo;
        this.idIncentivo = idIncentivo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.metaCantidad = metaCantidad;
        this.metaUnidad = metaUnidad;
        this.cantidadActual = cantidadActual;
        this.estado = estado;
        this.completadoEn = completadoEn;
        this.puedeReclamar = puedeReclamar;
    }

    public Long getIdUsuarioIncentivo() {
        return idUsuarioIncentivo;
    }

    public void setIdUsuarioIncentivo(Long idUsuarioIncentivo) {
        this.idUsuarioIncentivo = idUsuarioIncentivo;
    }

    public Long getIdIncentivo() {
        return idIncentivo;
    }

    public void setIdIncentivo(Long idIncentivo) {
        this.idIncentivo = idIncentivo;
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

    public Integer getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(Integer cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getCompletadoEn() {
        return completadoEn;
    }

    public void setCompletadoEn(LocalDateTime completadoEn) {
        this.completadoEn = completadoEn;
    }

    public Boolean getPuedeReclamar() {
        return puedeReclamar;
    }

    public void setPuedeReclamar(Boolean puedeReclamar) {
        this.puedeReclamar = puedeReclamar;
    }
}
