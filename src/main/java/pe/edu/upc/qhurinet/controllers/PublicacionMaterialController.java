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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.PublicacionMaterialDTO;
import pe.edu.upc.qhurinet.entities.Material;
import pe.edu.upc.qhurinet.entities.Publicacion;
import pe.edu.upc.qhurinet.entities.PublicacionMaterial;
import pe.edu.upc.qhurinet.entities.PublicacionMaterialId;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.IMaterialService;
import pe.edu.upc.qhurinet.servicesinterfaces.IPublicacionMaterialService;
import pe.edu.upc.qhurinet.servicesinterfaces.IPublicacionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/publicaciones/{idPublicacion}/materiales")
public class PublicacionMaterialController {
    @Autowired
    private IPublicacionService pS;

    @Autowired
    private IMaterialService mS;

    @Autowired
    private IPublicacionMaterialService pMS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<PublicacionMaterialDTO>> materialesPublicacion(@PathVariable Long idPublicacion) {
        List<PublicacionMaterialDTO> lista = pMS.materialesPorPublicacion(idPublicacion)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> registrarMaterial(@PathVariable Long idPublicacion, @RequestBody PublicacionMaterialDTO dto) {
        Optional<Publicacion> publicacion = pS.listId(idPublicacion);
        if (publicacion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publicacion no encontrada");
        }
        securityUserResolver.requireAdminOrUser(publicacion.get().getUsuario().getId());

        Optional<Material> material = mS.listId(dto.getIdMaterial());
        if (material.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material no encontrado");
        }

        PublicacionMaterialId id = new PublicacionMaterialId(publicacion.get().getId(), material.get().getId());
        PublicacionMaterial pm = pMS.listId(id).orElseGet(PublicacionMaterial::new);
        pm.setId(id);
        pm.setPublicacion(publicacion.get());
        pm.setMaterial(material.get());
        pm.setCantidad(dto.getCantidad());
        pm.setUnidad(dto.getUnidad() == null ? "kg" : dto.getUnidad());

        PublicacionMaterial cur = pMS.insert(pm);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(cur));
    }

    @DeleteMapping("/{idMaterial}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> eliminarMaterial(@PathVariable Long idPublicacion, @PathVariable Long idMaterial) {
        PublicacionMaterialId id = new PublicacionMaterialId(idPublicacion, idMaterial);
        if (pMS.listId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material de publicacion no encontrado");
        }
        Publicacion publicacion = pS.listId(idPublicacion).orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));
        securityUserResolver.requireAdminOrUser(publicacion.getUsuario().getId());

        pMS.delete(id);
        return ResponseEntity.ok("Material de publicacion eliminado correctamente");
    }

    private PublicacionMaterialDTO toDTO(PublicacionMaterial publicacionMaterial) {
        ModelMapper m = new ModelMapper();
        PublicacionMaterialDTO dto = m.map(publicacionMaterial, PublicacionMaterialDTO.class);
        dto.setIdPublicacion(publicacionMaterial.getPublicacion().getId());
        dto.setIdMaterial(publicacionMaterial.getMaterial().getId());
        return dto;
    }
}
