package pe.edu.upc.qhurinet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PublicacionMaterialId implements Serializable {
    @Column(name = "id_publicacion")
    private Long publicacionId;

    @Column(name = "id_material")
    private Long materialId;

    public PublicacionMaterialId() {
    }

    public PublicacionMaterialId(Long publicacionId, Long materialId) {
        this.publicacionId = publicacionId;
        this.materialId = materialId;
    }

    public Long getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Long publicacionId) {
        this.publicacionId = publicacionId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PublicacionMaterialId that)) {
            return false;
        }
        return Objects.equals(publicacionId, that.publicacionId) && Objects.equals(materialId, that.materialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicacionId, materialId);
    }
}
