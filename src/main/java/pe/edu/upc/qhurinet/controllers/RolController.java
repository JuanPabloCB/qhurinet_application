package pe.edu.upc.qhurinet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.RolDTO;
import pe.edu.upc.qhurinet.entities.Rol;
import pe.edu.upc.qhurinet.servicesinterfaces.IRolService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RolController {
    @Autowired
    private IRolService rS;

    @GetMapping({"", "/lista"})
    public List<RolDTO> list() {
        return rS.list().stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public RolDTO listId(@PathVariable Long id) {
        return toDTO(rS.listId(id).orElseThrow(() -> new IllegalArgumentException("Rol no encontrado")));
    }

    @PostMapping({"", "/nuevo"})
    public RolDTO insert(@RequestBody RolDTO dto) {
        ModelMapper m = new ModelMapper();
        Rol rol = m.map(dto, Rol.class);
        return toDTO(rS.insert(rol));
    }

    @PutMapping("/{id}")
    public RolDTO update(@PathVariable Long id, @RequestBody RolDTO dto) {
        Rol rol = rS.listId(id).orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        rol.setNombre(dto.getNombre());
        rS.update(rol);
        return toDTO(rol);
    }

    @PutMapping("/actualiza")
    public RolDTO update(@RequestBody RolDTO dto) {
        return update(dto.getId(), dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        rS.delete(id);
    }

    private RolDTO toDTO(Rol rol) {
        ModelMapper m = new ModelMapper();
        return m.map(rol, RolDTO.class);
    }
}
