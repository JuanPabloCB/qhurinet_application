package pe.edu.upc.qhurinet.dtos;

import java.util.List;

public class PublicacionDetalleDTO {
    private PublicacionDTO publicacion;
    private List<PublicacionMaterialDetalleDTO> materiales;
    private Double reputacionPromedio;
    private Integer cantidadCalificaciones;

    public PublicacionDetalleDTO() {
    }

    public PublicacionDetalleDTO(PublicacionDTO publicacion, List<PublicacionMaterialDetalleDTO> materiales, Double reputacionPromedio, Integer cantidadCalificaciones) {
        this.publicacion = publicacion;
        this.materiales = materiales;
        this.reputacionPromedio = reputacionPromedio;
        this.cantidadCalificaciones = cantidadCalificaciones;
    }

    public PublicacionDTO getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(PublicacionDTO publicacion) {
        this.publicacion = publicacion;
    }

    public List<PublicacionMaterialDetalleDTO> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<PublicacionMaterialDetalleDTO> materiales) {
        this.materiales = materiales;
    }

    public Double getReputacionPromedio() {
        return reputacionPromedio;
    }

    public void setReputacionPromedio(Double reputacionPromedio) {
        this.reputacionPromedio = reputacionPromedio;
    }

    public Integer getCantidadCalificaciones() {
        return cantidadCalificaciones;
    }

    public void setCantidadCalificaciones(Integer cantidadCalificaciones) {
        this.cantidadCalificaciones = cantidadCalificaciones;
    }
}
