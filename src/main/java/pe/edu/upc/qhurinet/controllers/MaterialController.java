package pe.edu.upc.qhurinet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.MaterialDTO;
import pe.edu.upc.qhurinet.entities.Material;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.IMaterialService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/materiales")
public class MaterialController {
    @Autowired
    private IMaterialService mS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    public ResponseEntity<List<MaterialDTO>> listar() {
        List<MaterialDTO> lista = mS.list()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> registrar(@RequestBody MaterialDTO dto) {
        ModelMapper m = new ModelMapper();
        Material material = m.map(dto, Material.class);
        material.setUsuario(securityUserResolver.currentUser());
        Material cur = mS.insert(material);
        MaterialDTO responseDTO = toDTO(cur);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/mis-materiales")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MaterialDTO>> listarMisMateriales() {
        Usuario usuario = securityUserResolver.currentUser();
        List<MaterialDTO> lista = mS.list()
                .stream()
                .filter(material -> material.getUsuario() != null && usuario.getId().equals(material.getUsuario().getId()))
                .map(this::toDTO)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Material> material = mS.listId(id);

        if (material.isPresent()) {
            return ResponseEntity.ok(toDTO(material.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material no encontrado");
    }

    @PutMapping("/actualiza")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> actualizar(@RequestBody MaterialDTO dto) {
        Optional<Material> existente = mS.listId(dto.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material no encontrado");
        }

        Material material = existente.get();
        material.setNombre(dto.getNombre());
        material.setCategoria(dto.getCategoria());
        material.setDescripcion(dto.getDescripcion());
        material.setPuntosPorKg(dto.getPuntosPorKg());
        mS.update(material);

        return ResponseEntity.ok("Material actualizado correctamente");
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> actualizarPorId(@PathVariable Long id, @RequestBody MaterialDTO dto) {
        dto.setId(id);
        return actualizar(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<Material> material = mS.listId(id);
        if (material.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material no encontrado");
        }

        mS.delete(id);
        return ResponseEntity.ok("Material eliminado correctamente");
    }

    @GetMapping({"/clasificar", "/sugerencia"})
    public ResponseEntity<?> clasificar(@RequestParam("texto") String texto) {
        List<MaterialDTO> lista = mS.clasificar(texto)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Material no reconocido. Seleccione una categoria manualmente");
        }
        return ResponseEntity.ok(lista);
    }

    private MaterialDTO toDTO(Material material) {
        ModelMapper m = new ModelMapper();
        MaterialDTO dto = m.map(material, MaterialDTO.class);
        if (material.getUsuario() != null) {
            dto.setUsuarioId(material.getUsuario().getId());
        }
        return dto;
    }
}
