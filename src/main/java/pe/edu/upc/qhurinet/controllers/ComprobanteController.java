package pe.edu.upc.qhurinet.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/comprobantes")
@PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
public class ComprobanteController {
    @GetMapping("/{tipo}/{id}.csv")
    public ResponseEntity<byte[]> csv(@PathVariable String tipo, @PathVariable Long id) {
        String csv = "tipo,id,estado\n" + tipo + "," + id + ",generado\n";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante-" + tipo + "-" + id + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping("/{tipo}/{id}.pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable String tipo, @PathVariable Long id) {
        String contenido = "Comprobante QhuriNet\nTipo: " + tipo + "\nId: " + id + "\nEstado: generado";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante-" + tipo + "-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(contenido.getBytes(StandardCharsets.UTF_8));
    }
}
