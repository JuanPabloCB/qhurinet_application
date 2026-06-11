package pe.edu.upc.qhurinet.dtos;

public class DocumentoRevisionDTO {
    private String estado;
    private String motivoRechazo;

    public DocumentoRevisionDTO() {
    }

    public DocumentoRevisionDTO(String estado, String motivoRechazo) {
        this.estado = estado;
        this.motivoRechazo = motivoRechazo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }
}
