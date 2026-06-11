package pe.edu.upc.qhurinet.dtos;

public class AuthenticationResponseDTO {
    private String token;
    private Long expiresIn;
    private String tokenType;
    private String refreshToken;

    public AuthenticationResponseDTO() {
    }

    public AuthenticationResponseDTO(String token, Long expiresIn, String tokenType) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
    }

    public AuthenticationResponseDTO(String token, Long expiresIn, String tokenType, String refreshToken) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
