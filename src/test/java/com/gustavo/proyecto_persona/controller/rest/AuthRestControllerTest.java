package com.gustavo.proyecto_persona.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.proyecto_persona.dto.rest.AuthRequestDto;
import com.gustavo.proyecto_persona.service.DetalleUsuarioService;
import com.gustavo.proyecto_persona.service.JwtService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@WebMvcTest(AuthRestController.class)
public class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private DetalleUsuarioService detalleUsuarioService;

    @MockitoBean
    private JwtService jwtService;

    @Nested
    class LoginTests {

        @Test
        public void testLoginInternalErrorEnAuthentication() throws Exception {
            AuthRequestDto request = new AuthRequestDto("usuarioPrueba", "123456789");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new RuntimeException());

            mockMvc.perform(post("/api/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError());

            verifyNoInteractions(detalleUsuarioService);
            verifyNoInteractions(jwtService);
        }

        @Test
        public void testLoginRequest() throws Exception {

            AuthRequestDto authRequestDto = new AuthRequestDto("Gus123", "123456789");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(new TestingAuthenticationToken(authRequestDto.username(), authRequestDto.password(), "ROLE_ADMIN"));

            when(detalleUsuarioService.loadUserByUsername(authRequestDto.username()))
                    .thenReturn(User.builder()
                            .username("Gus123")
                            .password("123456789")
                            .roles("ADMIN")
                            .build()
                    );

            mockMvc.perform(post("/api/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequestDto)))
                    .andExpect(status().isOk());

            verify(jwtService, times(1)).generateToken(any(UserDetails.class));

        }
    }
}
