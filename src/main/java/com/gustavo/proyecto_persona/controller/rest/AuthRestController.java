package com.gustavo.proyecto_persona.controller.rest;

import com.gustavo.proyecto_persona.dto.rest.AuthRequestDto;
import com.gustavo.proyecto_persona.dto.rest.AuthResponseDto;
import com.gustavo.proyecto_persona.service.DetalleUsuarioService;
import com.gustavo.proyecto_persona.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final DetalleUsuarioService detalleUsuarioService;
    private final JwtService jwtService;

    public AuthRestController(AuthenticationManager authenticationManager, DetalleUsuarioService detalleUsuarioService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.detalleUsuarioService = detalleUsuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        // 1. Spring Security intenta autenticar con usuario y contraseña
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. Si pasa, buscamos los detalles del usuario
        UserDetails userDetails = detalleUsuarioService.loadUserByUsername(request.username());

        // 3. Generamos el Token
        String jwtToken = jwtService.generateToken(userDetails);

        // 4. Devolvemos el token al cliente
        return ResponseEntity.ok(new AuthResponseDto(jwtToken));
    }
}
