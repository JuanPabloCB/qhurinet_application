package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.SoporteContacto;
import pe.edu.upc.qhurinet.repositories.ISoporteContactoRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.ISoporteContactoService;

import java.util.List;
import java.util.Optional;

@Service
public class SoporteContactoServiceImplement implements ISoporteContactoService {
    @Autowired
    private ISoporteContactoRepository sR;

    @Override
    public List<SoporteContacto> list() {
        return sR.findAll();
    }

    @Override
    public SoporteContacto insert(SoporteContacto s) {
        return sR.save(s);
    }

    @Override
    public Optional<SoporteContacto> listId(Long id) {
        return sR.findById(id);
    }

    @Override
    public void update(SoporteContacto s) {
        sR.save(s);
    }

    @Override
    public void delete(Long id) {
        sR.deleteById(id);
    }

    @Override
    public List<SoporteContacto> activos() {
        return sR.findByActivoTrueOrderByTipoAsc();
    }
}
