package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.Rol;
import pe.edu.upc.qhurinet.repositories.IRolRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IRolService;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImplement implements IRolService {
    @Autowired
    private IRolRepository rR;

    @Override
    public List<Rol> list() {
        return rR.findAll();
    }

    @Override
    public Rol insert(Rol r) {
        return rR.save(r);
    }

    @Override
    public Optional<Rol> listId(Long id) {
        return rR.findById(id);
    }

    @Override
    public Optional<Rol> findByNombre(String nombre) {
        return rR.findByNombre(nombre);
    }

    @Override
    public void update(Rol r) {
        rR.save(r);
    }

    @Override
    public void delete(Long id) {
        rR.deleteById(id);
    }
}
