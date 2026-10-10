package com.gustavo.proyecto_persona.service;

import com.gustavo.proyecto_persona.DataProvider;
import com.gustavo.proyecto_persona.repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DetalletUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DetalleUsuarioService detalleUsuarioService;

    @Nested
    class LoadUsuarioServiceTests {
        @Test
        public void testLoadUserByUsernameNoEncontrado() {
            String username = "Gus123";
            when(usuarioRepository.findByUsername(username))
                    .thenReturn(Optional.empty());

            UsernameNotFoundException resultado = assertThrows(UsernameNotFoundException.class,
                    () -> detalleUsuarioService.loadUserByUsername(username));

            assertEquals("Usuario no encontrado: " + username, resultado.getMessage());
            verify(usuarioRepository, times(1)).findByUsername(username);

        }

        @Test
        public void testLoadUserByUsername() {
            String username = "Gus123";
            when(usuarioRepository.findByUsername(username))
                    .thenReturn(Optional.of(DataProvider.getUsuarioMock()));

            UserDetails resultado = detalleUsuarioService.loadUserByUsername(username);

            assertNotNull(resultado);
            assertEquals(username, resultado.getUsername());

        }
    }
}
