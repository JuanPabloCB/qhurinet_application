package pe.edu.upc.qhurinet.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaccionDineroDTO {
    private Long id;
    private Long idUsuario;
    private String tipo;
    private BigDecimal monto;
    private String moneda;
    private String estado;
    private String concepto;
    private String metodoPagoTipo;
    private String metodoPagoDetalle;
    private String referenciaExterna;
    private LocalDateTime createdAt;

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

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getMetodoPagoTipo() {
        return metodoPagoTipo;
    }

    public void setMetodoPagoTipo(String metodoPagoTipo) {
        this.metodoPagoTipo = metodoPagoTipo;
    }

    public String getMetodoPagoDetalle() {
        return metodoPagoDetalle;
    }

    public void setMetodoPagoDetalle(String metodoPagoDetalle) {
        this.metodoPagoDetalle = metodoPagoDetalle;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
