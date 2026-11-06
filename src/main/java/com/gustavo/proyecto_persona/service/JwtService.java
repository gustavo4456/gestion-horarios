package com.gustavo.proyecto_persona.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "VGhpcy1pcy1hLXZlcnktc2VjdXJlLWtleS1mb3Itand0LWluLXNwcmluZy1ib290LWFwcGxpY2F0aW9u";

    // 1. Obtener el usuario (subject) desde el token
    public String extractUsername(String token) {

        if (token == null) {
            throw new IllegalArgumentException("El token no puede ser null.");
        }

        if (token.isBlank()){
            throw new IllegalArgumentException("El token no puede estar vacio.");

        }

        if (!token.contains(".") || token.chars().filter(ch -> ch == '.').count() != 2) {
            throw new MalformedJwtException("El token JWT no tiene un formato válido.");
        }

        return extractClaim(token, Claims::getSubject);
    }

    // 2. Generar el Token (Pase VIP)
    public String generateToken(UserDetails userDetails) {

        if (userDetails == null){
            throw new IllegalArgumentException("UserDetails no puede ser nulo.");
        }

        return Jwts.builder()
                .claims(new HashMap<>())
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(getSignInKey())
                .compact();
    }

    // 3. Validar si el token es de quien dice ser y no está vencido
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("UserDetails no puede ser nulo.");
        }

        try {
            final String username = extractUsername(token);
            // Si llegó hasta aquí, el token NO está expirado (jjwt ya lo validó).
            // Solo falta comprobar que el usuario coincida.
            return username.equals(userDetails.getUsername());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return false;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
