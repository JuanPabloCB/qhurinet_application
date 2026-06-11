package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;

public class HistorialMaterialDTO {
    private String material;
    private String categoria;
    private Long cantidad;
    private LocalDateTime ultimaPublicacion;

    public HistorialMaterialDTO() {
    }

    public HistorialMaterialDTO(String material, String categoria, Long cantidad, LocalDateTime ultimaPublicacion) {
        this.material = material;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.ultimaPublicacion = ultimaPublicacion;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getUltimaPublicacion() {
        return ultimaPublicacion;
    }

    public void setUltimaPublicacion(LocalDateTime ultimaPublicacion) {
        this.ultimaPublicacion = ultimaPublicacion;
    }
}
