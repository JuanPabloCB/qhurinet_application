package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.Certificado;
import pe.edu.upc.qhurinet.repositories.ICertificadoRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.ICertificadoService;

import java.util.List;
import java.util.Optional;

@Service
public class CertificadoServiceImplement implements ICertificadoService {
    @Autowired
    private ICertificadoRepository cR;

    @Override
    public List<Certificado> list() {
        return cR.findAll();
    }

    @Override
    public Certificado insert(Certificado c) {
        return cR.save(c);
    }

    @Override
    public Optional<Certificado> listId(Long id) {
        return cR.findById(id);
    }

    @Override
    public void update(Certificado c) {
        cR.save(c);
    }

    @Override
    public void delete(Long id) {
        cR.deleteById(id);
    }

    @Override
    public List<Certificado> certificadosPorUsuario(Long idUsuario) {
        return cR.findByUsuarioIdOrderByFechaObtencionDesc(idUsuario);
    }

    @Override
    public List<Certificado> certificadosPorDificultad(String nivelDificultad) {
        return cR.findByNivelDificultadIgnoreCaseOrderByFechaObtencionDesc(nivelDificultad);
    }
}
