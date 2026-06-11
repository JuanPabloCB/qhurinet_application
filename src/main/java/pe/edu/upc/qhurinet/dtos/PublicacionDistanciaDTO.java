package pe.edu.upc.qhurinet.dtos;

public class PublicacionDistanciaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String material;
    private String tipoPunto;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Double distanciaKm;

    public PublicacionDistanciaDTO() {
    }

    public PublicacionDistanciaDTO(Long id, String titulo, String descripcion, String categoria, String material, String tipoPunto, String direccion, Double latitud, Double longitud, Double distanciaKm) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.material = material;
        this.tipoPunto = tipoPunto;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.distanciaKm = distanciaKm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getTipoPunto() {
        return tipoPunto;
    }

    public void setTipoPunto(String tipoPunto) {
        this.tipoPunto = tipoPunto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }
}
