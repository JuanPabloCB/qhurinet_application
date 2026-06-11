package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.Certificado;

import java.util.List;
import java.util.Optional;

public interface ICertificadoService {
    public List<Certificado> list();
    public Certificado insert(Certificado c);
    public Optional<Certificado> listId(Long id);
    public void update(Certificado c);
    public void delete(Long id);
    public List<Certificado> certificadosPorUsuario(Long idUsuario);
    public List<Certificado> certificadosPorDificultad(String nivelDificultad);
}
