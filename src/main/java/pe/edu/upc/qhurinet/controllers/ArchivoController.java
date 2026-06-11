package pe.edu.upc.qhurinet.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Set;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/archivos")
@PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
public class ArchivoController {
    private static final long MAX_FILE_SIZE = 5L * 1024L * 1024L;
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${qhurinet.upload-dir:uploads}")
    private String uploadDir;

    @PostMapping
    public Map<String, Object> subir(@RequestParam("file") MultipartFile file) throws java.io.IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacio");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo no puede superar 5 MB");
        }
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Solo se permiten imagenes JPG, PNG, WEBP o GIF");
        }
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);
        String nombre = token() + "-" + sanitize(file.getOriginalFilename());
        Path destino = dir.resolve(nombre).normalize();
        if (!destino.startsWith(dir)) {
            throw new IllegalArgumentException("Nombre de archivo invalido");
        }
        file.transferTo(destino);
        return Map.of("nombre", nombre, "url", "/uploads/" + nombre, "size", file.getSize(), "contentType", file.getContentType());
    }

    @DeleteMapping("/{nombre}")
    public Map<String, Object> eliminar(@PathVariable String nombre) throws java.io.IOException {
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path destino = dir.resolve(sanitize(nombre)).normalize();
        if (!destino.startsWith(dir)) {
            throw new IllegalArgumentException("Nombre de archivo invalido");
        }
        boolean eliminado = Files.deleteIfExists(destino);
        return Map.of("nombre", nombre, "eliminado", eliminado);
    }

    private String token() {
        byte[] bytes = new byte[12];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String sanitize(String name) {
        if (name == null || name.isBlank()) {
            return "archivo.bin";
        }
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
