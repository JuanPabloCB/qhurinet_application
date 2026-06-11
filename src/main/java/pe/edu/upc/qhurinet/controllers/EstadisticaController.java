package pe.edu.upc.qhurinet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.servicesinterfaces.IEstadisticaService;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticaController {
    @Autowired
    private IEstadisticaService eS;

    @GetMapping({"", "/globales"})
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> globales() {
        return ResponseEntity.ok(eS.globales());
    }

    @GetMapping("/materiales")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> materialesReciclados() {
        return ResponseEntity.ok(eS.materialesReciclados());
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
    public ResponseEntity<?> reporteUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(eS.reporteUsuario(idUsuario));
    }
}
