package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;

public class PublicacionCategoriaMaterialDTO {
    private String categoria;
    private String material;
    private Long cantidad;
    private LocalDateTime ultimaPublicacion;

    public PublicacionCategoriaMaterialDTO() {
    }

    public PublicacionCategoriaMaterialDTO(String categoria, String material, Long cantidad, LocalDateTime ultimaPublicacion) {
        this.categoria = categoria;
        this.material = material;
        this.cantidad = cantidad;
        this.ultimaPublicacion = ultimaPublicacion;
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
