package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.MetodoPago;
import pe.edu.upc.qhurinet.repositories.IMetodoPagoRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IMetodoPagoService;

import java.util.List;
import java.util.Optional;

@Service
public class MetodoPagoServiceImplement implements IMetodoPagoService {
    @Autowired
    private IMetodoPagoRepository mR;

    @Override
    public List<MetodoPago> list() {
        return mR.findAll();
    }

    @Override
    public MetodoPago insert(MetodoPago m) {
        return mR.save(m);
    }

    @Override
    public Optional<MetodoPago> listId(Long id) {
        return mR.findById(id);
    }

    @Override
    public void update(MetodoPago m) {
        mR.save(m);
    }

    @Override
    public void delete(Long id) {
        mR.deleteById(id);
    }

    @Override
    public List<MetodoPago> metodosPorUsuario(Long idUsuario) {
        return mR.findByUsuarioIdOrderByPrincipalDescCreatedAtDesc(idUsuario);
    }
}
