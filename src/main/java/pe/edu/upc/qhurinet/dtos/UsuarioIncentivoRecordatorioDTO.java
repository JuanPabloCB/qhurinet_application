package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuarioIncentivoRecordatorioDTO {
    private Long idUsuarioIncentivo;
    private Long idIncentivo;
    private String nombre;
    private String tipo;
    private Integer metaCantidad;
    private Integer cantidadActual;
    private String estado;
    private LocalDate fechaFin;
    private LocalDateTime completadoEn;
    private Boolean puedeReclamar;
    private Boolean proximoAVencer;

    public UsuarioIncentivoRecordatorioDTO() {
    }

    public UsuarioIncentivoRecordatorioDTO(Long idUsuarioIncentivo, Long idIncentivo, String nombre, String tipo, Integer metaCantidad, Integer cantidadActual, String estado, LocalDate fechaFin, LocalDateTime completadoEn, Boolean puedeReclamar, Boolean proximoAVencer) {
        this.idUsuarioIncentivo = idUsuarioIncentivo;
        this.idIncentivo = idIncentivo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.metaCantidad = metaCantidad;
        this.cantidadActual = cantidadActual;
        this.estado = estado;
        this.fechaFin = fechaFin;
        this.completadoEn = completadoEn;
        this.puedeReclamar = puedeReclamar;
        this.proximoAVencer = proximoAVencer;
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

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
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

    public Boolean getProximoAVencer() {
        return proximoAVencer;
    }

    public void setProximoAVencer(Boolean proximoAVencer) {
        this.proximoAVencer = proximoAVencer;
    }
}
