package pe.edu.upc.qhurinet.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_incentivo")
public class UsuarioIncentivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_incentivo", nullable = false)
    private Incentivo incentivo;

    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadActual;

    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    @Column(name = "completado_en")
    private LocalDateTime completadoEn;

    public UsuarioIncentivo() {
    }

    public UsuarioIncentivo(Long id, Usuario usuario, Incentivo incentivo, Integer cantidadActual, String estado, LocalDateTime completadoEn) {
        this.id = id;
        this.usuario = usuario;
        this.incentivo = incentivo;
        this.cantidadActual = cantidadActual;
        this.estado = estado;
        this.completadoEn = completadoEn;
    }

    @PrePersist
    public void prePersist() {
        if (this.cantidadActual == null) {
            this.cantidadActual = 0;
        }
        if (this.estado == null) {
            this.estado = "en_progreso";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Incentivo getIncentivo() {
        return incentivo;
    }

    public void setIncentivo(Incentivo incentivo) {
        this.incentivo = incentivo;
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
