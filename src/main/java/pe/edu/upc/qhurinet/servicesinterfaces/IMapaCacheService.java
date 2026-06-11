package pe.edu.upc.qhurinet.servicesinterfaces;

import pe.edu.upc.qhurinet.entities.MapaCache;

import java.util.List;
import java.util.Optional;

public interface IMapaCacheService {
    public List<MapaCache> list();
    public MapaCache insert(MapaCache m);
    public Optional<MapaCache> listId(Long id);
    public Optional<MapaCache> findByCacheKey(String cacheKey);
    public Optional<MapaCache> findActiveByCacheKey(String cacheKey);
    public void update(MapaCache m);
    public void delete(Long id);
}
