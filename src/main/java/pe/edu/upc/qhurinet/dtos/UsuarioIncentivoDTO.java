package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;

public class UsuarioIncentivoDTO {
    private Long id;
    private Long idUsuario;
    private String usuarioNombre;
    private Long idIncentivo;
    private String incentivoNombre;
    private Integer cantidadActual;
    private String estado;
    private LocalDateTime completadoEn;

    public UsuarioIncentivoDTO() {
    }

    public UsuarioIncentivoDTO(Long id, Long idUsuario, String usuarioNombre, Long idIncentivo, String incentivoNombre, Integer cantidadActual, String estado, LocalDateTime completadoEn) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.usuarioNombre = usuarioNombre;
        this.idIncentivo = idIncentivo;
        this.incentivoNombre = incentivoNombre;
        this.cantidadActual = cantidadActual;
        this.estado = estado;
        this.completadoEn = completadoEn;
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

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public Long getIdIncentivo() {
        return idIncentivo;
    }

    public void setIdIncentivo(Long idIncentivo) {
        this.idIncentivo = idIncentivo;
    }

    public String getIncentivoNombre() {
        return incentivoNombre;
    }

    public void setIncentivoNombre(String incentivoNombre) {
        this.incentivoNombre = incentivoNombre;
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
}
