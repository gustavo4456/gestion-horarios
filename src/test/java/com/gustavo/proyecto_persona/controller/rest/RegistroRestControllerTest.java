package com.gustavo.proyecto_persona.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.proyecto_persona.model.Usuario;
import com.gustavo.proyecto_persona.service.DetalleUsuarioService;
import com.gustavo.proyecto_persona.service.JwtService;
import com.gustavo.proyecto_persona.service.RegistroService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;


@WebMvcTest(RegistroRestController.class)
public class RegistroRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private RegistroService registroService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private DetalleUsuarioService detalleUsuarioService;

    @Nested
    class GuardarUsuarioTests {

        @Test
        public void testGuardarUsuarioNull() throws Exception {
            Usuario usuario = new Usuario(null, null, "123456789", "ADMIN", null, null);

            mockMvc.perform(post("/api/usuario/registro")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuario)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Datos inválidos"))
                    .andExpect(jsonPath("$.mensaje").value("El nombre de usuario es obligatorio."));

            verifyNoInteractions(registroService);
        }

        @Test
        public void testGuardarUsuario() throws Exception {
            Usuario usuario = new Usuario(null, "Gus123", "123456789", "ADMIN", null, null);

            mockMvc.perform(post("/api/usuario/registro")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuario)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Se creó un nuevo usuario."));

            verify(registroService, times(1)).guardarUsuario(any(Usuario.class));
        }
    }
}
