package pe.edu.upc.qhurinet.dtos;

import java.util.List;

public class RecoleccionDetalleDTO {
    private RecoleccionDTO recoleccion;
    private PublicacionDTO publicacion;
    private Long idGenerador;
    private String generadorNombre;
    private Long idRecolector;
    private String recolectorNombre;
    private List<PublicacionMaterialDetalleDTO> materiales;
    private List<CalificacionDTO> calificaciones;

    public RecoleccionDetalleDTO() {
    }

    public RecoleccionDTO getRecoleccion() {
        return recoleccion;
    }

    public void setRecoleccion(RecoleccionDTO recoleccion) {
        this.recoleccion = recoleccion;
    }

    public PublicacionDTO getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(PublicacionDTO publicacion) {
        this.publicacion = publicacion;
    }

    public Long getIdGenerador() {
        return idGenerador;
    }

    public void setIdGenerador(Long idGenerador) {
        this.idGenerador = idGenerador;
    }

    public String getGeneradorNombre() {
        return generadorNombre;
    }

    public void setGeneradorNombre(String generadorNombre) {
        this.generadorNombre = generadorNombre;
    }

    public Long getIdRecolector() {
        return idRecolector;
    }

    public void setIdRecolector(Long idRecolector) {
        this.idRecolector = idRecolector;
    }

    public String getRecolectorNombre() {
        return recolectorNombre;
    }

    public void setRecolectorNombre(String recolectorNombre) {
        this.recolectorNombre = recolectorNombre;
    }

    public List<PublicacionMaterialDetalleDTO> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<PublicacionMaterialDetalleDTO> materiales) {
        this.materiales = materiales;
    }

    public List<CalificacionDTO> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<CalificacionDTO> calificaciones) {
        this.calificaciones = calificaciones;
    }
}
