package pe.edu.upc.qhurinet.dtos;

import java.time.LocalDateTime;

public class MapaCacheDTO {
    private Long id;
    private String cacheKey;
    private String contenidoJson;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public MapaCacheDTO() {
    }

    public MapaCacheDTO(Long id, String cacheKey, String contenidoJson, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.id = id;
        this.cacheKey = cacheKey;
        this.contenidoJson = contenidoJson;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getContenidoJson() {
        return contenidoJson;
    }

    public void setContenidoJson(String contenidoJson) {
        this.contenidoJson = contenidoJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
