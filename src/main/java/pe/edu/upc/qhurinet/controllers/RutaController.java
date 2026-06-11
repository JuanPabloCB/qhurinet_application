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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.RutaDTO;
import pe.edu.upc.qhurinet.entities.Publicacion;
import pe.edu.upc.qhurinet.entities.Ruta;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.servicesinterfaces.IPublicacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.IRutaService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {
    @Autowired
    private IRutaService rS;

    @Autowired
    private IUsuarioService uS;

    @Autowired
    private IPublicacionService pS;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<List<RutaDTO>> listar() {
        List<RutaDTO> lista = rS.list().stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> rutasPorUsuario(@PathVariable Long idUsuario) {
        List<RutaDTO> lista = rS.rutasPorUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay rutas registradas");
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping({"", "/nuevo"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> registrar(@RequestBody RutaDTO dto) {
        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        ModelMapper m = new ModelMapper();
        Ruta ruta = m.map(dto, Ruta.class);
        ruta.setUsuario(usuario.get());

        Ruta cur = rS.insert(ruta);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(cur));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        Optional<Ruta> ruta = rS.listId(id);
        if (ruta.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruta no encontrada");
        }
        rS.delete(id);
        return ResponseEntity.ok("Ruta eliminada correctamente");
    }

    @GetMapping("/optima")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> rutaOptima(@RequestParam Double lat,
                                        @RequestParam Double lng,
                                        @RequestParam(defaultValue = "5") Integer limite) {
        List<Publicacion> puntos = pS.list().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .sorted(Comparator.comparingDouble(p -> distanciaKm(lat, lng, p.getLatitud(), p.getLongitud())))
                .limit(limite)
                .toList();

        double distanciaTotal = 0;
        double actualLat = lat;
        double actualLng = lng;
        for (Publicacion punto : puntos) {
            distanciaTotal += distanciaKm(actualLat, actualLng, punto.getLatitud(), punto.getLongitud());
            actualLat = punto.getLatitud();
            actualLng = punto.getLongitud();
        }

        return ResponseEntity.ok(Map.of(
                "algoritmo", "dijkstra-haversine-simplificado",
                "distanciaTotalKm", Math.round(distanciaTotal * 100.0) / 100.0,
                "puntos", puntos.stream()
                        .map(p -> Map.of("id", p.getId(), "titulo", p.getTitulo(), "latitud", p.getLatitud(), "longitud", p.getLongitud()))
                        .toList()
        ));
    }

    @GetMapping("/{id}/detalle")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Ruta> ruta = rS.listId(id);
        if (ruta.isPresent()) {
            return ResponseEntity.ok(toDTO(ruta.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruta no encontrada");
    }

    private double distanciaKm(Double lat1, Double lng1, Double lat2, Double lng2) {
        double radio = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return radio * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private RutaDTO toDTO(Ruta ruta) {
        ModelMapper m = new ModelMapper();
        RutaDTO dto = m.map(ruta, RutaDTO.class);
        dto.setIdUsuario(ruta.getUsuario().getId());
        return dto;
    }
}
