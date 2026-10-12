package com.gustavo.proyecto_persona.service;

import com.gustavo.proyecto_persona.DataProvider;
import com.gustavo.proyecto_persona.model.Usuario;
import com.gustavo.proyecto_persona.repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RegistroServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    RegistroService registroService;

    @Test
    public void testGuardarUsuarioPresente() {
        Usuario usuario = DataProvider.getUsuarioMock();
        when(usuarioRepository.findByUsername(usuario.getUsername()))
                .thenReturn(Optional.of(DataProvider.getUsuarioMock()));

        EntityNotFoundException resutlado = assertThrows(EntityNotFoundException.class,
                () -> registroService.guardarUsuario(usuario));

        assertEquals("El nombre de usuario ya está registrado.", resutlado.getMessage());
        verify(usuarioRepository, times(1))
                .findByUsername(usuario.getUsername());
        verify(usuarioRepository, times(0))
                .save(usuario);


    }

    @Test
    public void testGuardarUsuario() {
        Usuario usuario = DataProvider.getUsuarioMock();
        when(usuarioRepository.findByUsername(usuario.getUsername()))
                .thenReturn(Optional.empty());

        registroService.guardarUsuario(usuario);

        verify(usuarioRepository).findByUsername(usuario.getUsername());
        verify(usuarioRepository).save(usuario);


    }

    @Test
    public void testGuardarUsuarioNull() {
        Usuario usuario = null;


        IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                () -> registroService.guardarUsuario(usuario));

        verifyNoInteractions(usuarioRepository);
        assertEquals("El usuario no puede ser null.", resultado.getMessage());
    }
}
