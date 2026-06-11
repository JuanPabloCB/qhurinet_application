package pe.edu.upc.qhurinet.dtos;

import java.math.BigDecimal;

public class MaterialDTO {
    private Long id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private BigDecimal puntosPorKg;

    public MaterialDTO() {
    }

    public MaterialDTO(Long id, String nombre, String categoria, String descripcion, BigDecimal puntosPorKg) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.puntosPorKg = puntosPorKg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPuntosPorKg() {
        return puntosPorKg;
    }

    public void setPuntosPorKg(BigDecimal puntosPorKg) {
        this.puntosPorKg = puntosPorKg;
    }
}
