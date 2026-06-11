package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.qhurinet.entities.MapaCache;
import pe.edu.upc.qhurinet.repositories.IMapaCacheRepository;
import pe.edu.upc.qhurinet.servicesinterfaces.IMapaCacheService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MapaCacheServiceImplement implements IMapaCacheService {
    @Autowired
    private IMapaCacheRepository mR;

    @Override
    public List<MapaCache> list() {
        return mR.findAll();
    }

    @Override
    public MapaCache insert(MapaCache m) {
        return mR.save(m);
    }

    @Override
    public Optional<MapaCache> listId(Long id) {
        return mR.findById(id);
    }

    @Override
    public Optional<MapaCache> findByCacheKey(String cacheKey) {
        return mR.findByCacheKey(cacheKey);
    }

    @Override
    public Optional<MapaCache> findActiveByCacheKey(String cacheKey) {
        return mR.findTopByCacheKeyAndExpiresAtAfterOrderByCreatedAtDesc(cacheKey, LocalDateTime.now());
    }

    @Override
    public void update(MapaCache m) {
        mR.save(m);
    }

    @Override
    public void delete(Long id) {
        mR.deleteById(id);
    }
}
