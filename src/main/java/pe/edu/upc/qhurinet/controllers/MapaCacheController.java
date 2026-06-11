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
import pe.edu.upc.qhurinet.dtos.MapaCacheDTO;
import pe.edu.upc.qhurinet.entities.MapaCache;
import pe.edu.upc.qhurinet.servicesinterfaces.IMapaCacheService;

import java.util.List;

@RestController
@RequestMapping("/api/mapa-cache")
@PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
public class MapaCacheController {
    @Autowired
    private IMapaCacheService mS;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasRole('ADMIN')")
    public List<MapaCacheDTO> list() {
        return mS.list().stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MapaCacheDTO listId(@PathVariable Long id) {
        return toDTO(mS.listId(id).orElseThrow(() -> new IllegalArgumentException("Cache no encontrado")));
    }

    @GetMapping("/activa/{cacheKey}")
    public MapaCacheDTO findActiveByCacheKey(@PathVariable String cacheKey) {
        return toDTO(mS.findActiveByCacheKey(cacheKey).orElseThrow(() -> new IllegalArgumentException("Cache activo no encontrado")));
    }

    @PostMapping({"", "/nuevo"})
    public MapaCacheDTO insert(@RequestBody MapaCacheDTO dto) {
        return toDTO(mS.insert(toEntity(new MapaCache(), dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MapaCacheDTO update(@PathVariable Long id, @RequestBody MapaCacheDTO dto) {
        MapaCache mapaCache = mS.listId(id).orElseThrow(() -> new IllegalArgumentException("Cache no encontrado"));
        mS.update(toEntity(mapaCache, dto));
        return toDTO(mapaCache);
    }

    @PutMapping("/actualiza")
    @PreAuthorize("hasRole('ADMIN')")
    public MapaCacheDTO update(@RequestBody MapaCacheDTO dto) {
        return update(dto.getId(), dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        mS.delete(id);
    }

    private MapaCache toEntity(MapaCache mapaCache, MapaCacheDTO dto) {
        mapaCache.setCacheKey(dto.getCacheKey());
        mapaCache.setContenidoJson(dto.getContenidoJson());
        if (dto.getCreatedAt() != null) {
            mapaCache.setCreatedAt(dto.getCreatedAt());
        }
        if (dto.getExpiresAt() != null) {
            mapaCache.setExpiresAt(dto.getExpiresAt());
        }
        return mapaCache;
    }

    private MapaCacheDTO toDTO(MapaCache mapaCache) {
        ModelMapper m = new ModelMapper();
        return m.map(mapaCache, MapaCacheDTO.class);
    }
}
