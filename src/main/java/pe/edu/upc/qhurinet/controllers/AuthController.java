package pe.edu.upc.qhurinet.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.AuthenticationRequestDTO;
import pe.edu.upc.qhurinet.dtos.AuthenticationResponseDTO;
import pe.edu.upc.qhurinet.dtos.RefreshTokenRequestDTO;
import pe.edu.upc.qhurinet.dtos.RegisterRequestDTO;
import pe.edu.upc.qhurinet.dtos.UsuarioDTO;
import pe.edu.upc.qhurinet.entities.Rol;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.securities.JwtTokenUtil;
import pe.edu.upc.qhurinet.securities.LoginRateLimiter;
import pe.edu.upc.qhurinet.servicesimplements.JwtUserDetailsService;
import pe.edu.upc.qhurinet.servicesimplements.RefreshTokenService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private LoginRateLimiter loginRateLimiter;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDTO request, HttpServletRequest httpRequest) {
        String key = httpRequest.getRemoteAddr() + ":" + request.getUsername();
        if (!loginRateLimiter.allow(key)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of("error", "Demasiados intentos de login"));
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = refreshTokenService.create(userDetails.getUsername());
        return ResponseEntity.ok(new AuthenticationResponseDTO(token, jwtTokenUtil.getAccessTokenValiditySeconds(), "Bearer", refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequestDTO request) {
        return refreshTokenService.validate(request.getRefreshToken())
                .map(username -> {
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                    String token = jwtTokenUtil.generateToken(userDetails);
                    String refreshToken = refreshTokenService.create(username);
                    refreshTokenService.revoke(request.getRefreshToken());
                    return ResponseEntity.ok(new AuthenticationResponseDTO(token, jwtTokenUtil.getAccessTokenValiditySeconds(), "Bearer", refreshToken));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponseDTO(null, 0L, "Bearer", null)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody RefreshTokenRequestDTO request) {
        refreshTokenService.revoke(request.getRefreshToken());
        return ResponseEntity.ok(Map.of("message", "Sesion cerrada"));
    }

    @PostMapping("/register")
    public UsuarioDTO register(@RequestBody RegisterRequestDTO request) {
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(request.getPassword());
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        return toDTO(usuarioService.registrarUsuario(usuario));
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        Set<String> roles = usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet());
        return new UsuarioDTO(usuario.getId(), usuario.getUsername(), usuario.getNombre(), usuario.getCorreo(), usuario.getEnabled(), usuario.getCreatedAt(), roles);
    }
}
