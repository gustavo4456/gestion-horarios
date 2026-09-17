package com.gustavo.proyecto_persona.config.filter;

import com.gustavo.proyecto_persona.service.DetalleUsuarioService;
import com.gustavo.proyecto_persona.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final DetalleUsuarioService detalleUsuarioService;

    public JwtAuthenticationFilter(JwtService jwtService, DetalleUsuarioService detalleUsuarioService) {
        this.jwtService = jwtService;
        this.detalleUsuarioService = detalleUsuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtener el header llamado "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        try {
            // 2. Si no hay token o no empieza con "Bearer ", lo ignoramos y pasamos al siguiente filtro
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 3. Extraer el token (quitamos la palabra "Bearer ")
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);

            // 4. Si hay usuario y aún no está autenticado en este contexto
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.detalleUsuarioService.loadUserByUsername(username);

                // 5. Validamos el token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Creamos el token de Spring Security para decirle "Este usuario está logueado"
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Guardamos la sesión (contexto)
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Continuamos con la petición
            filterChain.doFilter(request, response);


        } catch (ExpiredJwtException e) {
            // 4. CAPTURAR TOKEN EXPIRADO
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"El token ha expirado\", \"status\": 401}");

        } catch (JwtException e) {
            // 5. CAPTURAR CUALQUIER OTRO ERROR DE TOKEN (mal formateado, firma inválida, etc.)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido o alterado\", \"status\": 401}");
        }


    }
}
