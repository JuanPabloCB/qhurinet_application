package pe.edu.upc.qhurinet.servicesimplements;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RefreshTokenService {
    private static final long REFRESH_VALIDITY_SECONDS = 7 * 24 * 60 * 60;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<String, RefreshTokenEntry> tokens = new ConcurrentHashMap<>();
    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();

    public String create(String username) {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokens.put(token, new RefreshTokenEntry(username, Instant.now().plusSeconds(REFRESH_VALIDITY_SECONDS)));
        return token;
    }

    public Optional<String> validate(String token) {
        if (token == null || revokedTokens.contains(token)) {
            return Optional.empty();
        }
        RefreshTokenEntry entry = tokens.get(token);
        if (entry == null || entry.getExpiresAt().isBefore(Instant.now())) {
            tokens.remove(token);
            return Optional.empty();
        }
        return Optional.of(entry.getUsername());
    }

    public void revoke(String token) {
        if (token != null) {
            revokedTokens.add(token);
            tokens.remove(token);
        }
    }

    private static class RefreshTokenEntry {
        private final String username;
        private final Instant expiresAt;

        private RefreshTokenEntry(String username, Instant expiresAt) {
            this.username = username;
            this.expiresAt = expiresAt;
        }

        public String getUsername() {
            return username;
        }

        public Instant getExpiresAt() {
            return expiresAt;
        }
    }
}
