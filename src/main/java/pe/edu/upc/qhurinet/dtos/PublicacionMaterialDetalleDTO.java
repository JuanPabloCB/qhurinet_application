package pe.edu.upc.qhurinet.dtos;

import java.math.BigDecimal;

public class PublicacionMaterialDetalleDTO {
    private Long idMaterial;
    private String nombreMaterial;
    private String categoriaMaterial;
    private BigDecimal cantidad;
    private String unidad;
    private BigDecimal puntosPorKg;
    private BigDecimal puntosEstimados;

    public PublicacionMaterialDetalleDTO() {
    }

    public PublicacionMaterialDetalleDTO(Long idMaterial, String nombreMaterial, String categoriaMaterial, BigDecimal cantidad, String unidad, BigDecimal puntosPorKg, BigDecimal puntosEstimados) {
        this.idMaterial = idMaterial;
        this.nombreMaterial = nombreMaterial;
        this.categoriaMaterial = categoriaMaterial;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.puntosPorKg = puntosPorKg;
        this.puntosEstimados = puntosEstimados;
    }

    public Long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNombreMaterial() {
        return nombreMaterial;
    }

    public void setNombreMaterial(String nombreMaterial) {
        this.nombreMaterial = nombreMaterial;
    }

    public String getCategoriaMaterial() {
        return categoriaMaterial;
    }

    public void setCategoriaMaterial(String categoriaMaterial) {
        this.categoriaMaterial = categoriaMaterial;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public BigDecimal getPuntosPorKg() {
        return puntosPorKg;
    }

    public void setPuntosPorKg(BigDecimal puntosPorKg) {
        this.puntosPorKg = puntosPorKg;
    }

    public BigDecimal getPuntosEstimados() {
        return puntosEstimados;
    }

    public void setPuntosEstimados(BigDecimal puntosEstimados) {
        this.puntosEstimados = puntosEstimados;
    }
}
