package com.gustavo.proyecto_persona.service;

import com.gustavo.proyecto_persona.DataProvider;
import com.gustavo.proyecto_persona.dto.rest.PerfilAEditarRDto;
import com.gustavo.proyecto_persona.dto.rest.PerfilAGuardarRDto;
import com.gustavo.proyecto_persona.dto.web.PerfilAEditarDto;
import com.gustavo.proyecto_persona.dto.web.PerfilAGuardarDto;
import com.gustavo.proyecto_persona.model.Perfil;
import com.gustavo.proyecto_persona.model.Usuario;
import com.gustavo.proyecto_persona.repository.PerfilRepository;
import com.gustavo.proyecto_persona.repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PerfilService perfilService;


    @Nested
    class GetPersonasTests {

        @Test
        public void testGetPersonas() {

            String username = "Gus123";

            when(perfilRepository.findByUsuarioUsername(username))
                    .thenReturn(DataProvider.getPerfilesMockParaFindByUsuarioUsername());

            List<Perfil> resultado = perfilService.getPersonas(username);

            ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);


            verify(perfilRepository).findByUsuarioUsername(argumentCaptor.capture());
            assertNotNull(argumentCaptor.getValue());
            assertEquals(argumentCaptor.getValue(), resultado.getFirst().getUsuario().getUsername());

            assertNotNull(resultado);
            assertFalse(resultado.isEmpty());
            assertEquals(1L, resultado.getFirst().getId());
            assertEquals("Gustavo", resultado.getFirst().getNombre());
        }

        @Test
        public void testGetPersonasUsernameNull() {

            String username = null;

            List<Perfil> resultado = perfilService.getPersonas(username);

            assertTrue(resultado.isEmpty());


        }
    }

    @Nested
    class GetPersonaPorIdTests {

        @Test
        public void testGetPersonaPorIdUsernameNull() {
            Long id = 1L;
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.getPersonaPorId(id, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());


        }

        @Test
        public void testGetPersonaPorIdNull() {
            Long id = null;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.getPersonaPorId(id, username));

            assertEquals("El id no puede ser nulo o negativo.", resultado.getMessage());


        }

        @Test
        public void testGetPersonaPorIdNegativo() {
            Long id = -2L;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.getPersonaPorId(id, username));

            assertEquals("El id no puede ser nulo o negativo.", resultado.getMessage());
        }

        @Test
        public void testGetPersonaPorIdCero() {
            Long id = 0L;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.getPersonaPorId(id, username));

            assertEquals("El id no puede ser nulo o negativo.", resultado.getMessage());
        }

        @Test
        public void testGetPersonaPorIdPersonaNoEncontrada() {
            Long id = 99999L;
            String username = "Gus123";
            when(perfilRepository.findByIdAndUsuarioUsername(id, username))
                    .thenReturn(Optional.empty());

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> perfilService.getPersonaPorId(id, username));

            assertEquals("Persona no encontrada en la base de datos o acceso denegado.", resultado.getMessage());

        }

        @Test
        public void testGetPersonaPorId() {
            Long id = 99999L;
            String username = "Gus123";
            when(perfilRepository.findByIdAndUsuarioUsername(id, username))
                    .thenReturn(Optional.of(DataProvider.getPerfilMock()));

            Perfil resultado = perfilService.getPersonaPorId(id, username);

            assertNotNull(resultado);
            assertEquals(id, resultado.getId());
            assertEquals(username, resultado.getUsuario().getUsername());

        }

    }

    @Nested
    class BorrarPersonaTests {

        @Test
        public void testBorrarPersonaUsernameNull() {
            String username = null;
            Long id = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.borrarPersona(id, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
        }

        @Test
        public void testBorrarPersonaIdNull() {
            String username = "Gus123";
            Long id = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.borrarPersona(id, username));

            assertEquals("El id no puede ser nulo y no puede ser menor o igual a cero.", resultado
                    .getMessage());
            verifyNoInteractions(perfilRepository);
        }

        @Test
        public void testBorrarPersonaIdCero() {
            String username = "Gus123";
            Long id = 0L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.borrarPersona(id, username));

            assertEquals("El id no puede ser nulo y no puede ser menor o igual a cero.", resultado
                    .getMessage());
            verifyNoInteractions(perfilRepository);
        }

        @Test
        public void testBorrarPersonaIdNegativo() {
            String username = "Gus123";
            Long id = -9L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.borrarPersona(id, username));

            assertEquals("El id no puede ser nulo y no puede ser menor o igual a cero.", resultado
                    .getMessage());
            verifyNoInteractions(perfilRepository);
        }

        @Test
        public void testBorrarPersonaPerfilNoEncontrado() {
            Long id = 9999999L;
            String username = "Gus123";
            when(perfilRepository.existsByIdAndUsuarioUsername(id, username))
                    .thenReturn(false);


            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> perfilService.borrarPersona(id, username));


            verify(perfilRepository, times(1))
                    .existsByIdAndUsuarioUsername(id, username);
            assertEquals("No se pudo borrar. El perfil no existe o no te pertenece.", resultado.getMessage());
            verify(perfilRepository, times(0))
                    .deleteById(anyLong());
        }

        @Test
        public void testBorrarPersona() {
            Long id = 1L;
            String username = "Gus123";
            when(perfilRepository.existsByIdAndUsuarioUsername(id, username))
                    .thenReturn(true);

            perfilService.borrarPersona(id, username);

            verify(perfilRepository).existsByIdAndUsuarioUsername(id, username);
            verify(perfilRepository).deleteById(id);
        }
    }

    @Nested
    class GuardarPersonaTests {
        @Test
        public void testGuardarPersonaNull() {
            String username = "Gus123";
            PerfilAGuardarDto perfilDto = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.guardarPersona(perfilDto, username));

            assertEquals("El perfil no debe ser nulo.", resultado.getMessage());
            verifyNoInteractions(usuarioRepository);
            verifyNoInteractions(perfilRepository);
        }

        @Test
        public void testGuardarPersonaUsernameNull() {
            String username = null;
            PerfilAGuardarDto perfilDto = new PerfilAGuardarDto();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.guardarPersona(perfilDto, username));

            assertEquals("El username no debe ser nulo.", resultado.getMessage());
            verifyNoInteractions(usuarioRepository);
            verifyNoInteractions(perfilRepository);
        }

        @Test
        public void testGuardarPersonaPerfilNoEncontrado() {
            PerfilAGuardarDto perfilDto = new PerfilAGuardarDto();
            perfilDto.setNombre("universidad");

            String username = "1209210597210&&&&";

            when(usuarioRepository.findByUsername(username))
                    .thenReturn(Optional.empty());

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> perfilService.guardarPersona(perfilDto, username));

            assertEquals("No se encontró el usuario de la sesión actual.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
        }

        @Test
        public void testGuardarPesona() {
            PerfilAGuardarDto perfilDto = new PerfilAGuardarDto();
            perfilDto.setNombre("universidad");

            String username = "Gus123";

            when(usuarioRepository.findByUsername(username))
                    .thenReturn(Optional.of(DataProvider.getUsuarioMock()));


            perfilService.guardarPersona(perfilDto, username);

            ArgumentCaptor<Perfil> argumentPerfil = ArgumentCaptor.forClass(Perfil.class);

            verify(usuarioRepository).findByUsername(username);
            verify(perfilRepository).save(argumentPerfil.capture());
            assertNotNull(argumentPerfil);
            assertEquals("universidad", argumentPerfil.getValue().getNombre());
            assertEquals("Gus123", argumentPerfil.getValue().getUsuario().getUsername());
            assertNull(argumentPerfil.getValue().getId());


        }
    }

    @Nested
    class GuardarPersonaRestTests {
        @Test
        public void testGuardarPersonaNull() {
            String username = "Gus123";
            PerfilAGuardarRDto perfilDto = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.guardarPersonaRest(perfilDto, username));

            assertEquals("El perfil no debe ser nulo.", resultado.getMessage());
            verifyNoInteractions(usuarioRepository);
            verifyNoInteractions(perfilRepository);
        }

        @Test
        public void testGuardarPersonaUsernameNull() {
            String username = null;
            PerfilAGuardarRDto perfilDto = new PerfilAGuardarRDto();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.guardarPersonaRest(perfilDto, username));

            assertEquals("El username no debe ser nulo.", resultado.getMessage());
            verifyNoInteractions(usuarioRepository);
            verifyNoInteractions(perfilRepository);
        }

        @Test
        public void testGuardarPersonaPerfilNoEncontrado() {
            PerfilAGuardarRDto perfilDto = new PerfilAGuardarRDto();
            perfilDto.setNombre("universidad");

            String username = "1209210597210&&&&";

            when(usuarioRepository.findByUsername(username))
                    .thenReturn(Optional.empty());

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> perfilService.guardarPersonaRest(perfilDto, username));

            assertEquals("No se encontró el usuario de la sesión actual.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
        }

        @Test
        public void testGuardarPesona() {
            PerfilAGuardarRDto perfilDto = new PerfilAGuardarRDto();
            perfilDto.setNombre("universidad");

            String username = "Gus123";

            when(usuarioRepository.findByUsername(username))
                    .thenReturn(Optional.of(DataProvider.getUsuarioMock()));


            perfilService.guardarPersonaRest(perfilDto, username);

            ArgumentCaptor<Perfil> argumentPerfil = ArgumentCaptor.forClass(Perfil.class);

            verify(usuarioRepository).findByUsername(username);
            verify(perfilRepository).save(argumentPerfil.capture());
            assertNotNull(argumentPerfil);
            assertEquals("universidad", argumentPerfil.getValue().getNombre());
            assertEquals("Gus123", argumentPerfil.getValue().getUsuario().getUsername());
            assertNull(argumentPerfil.getValue().getId());


        }
    }

    @Nested
    class ActualizarPersonaTests {
        @Test
        public void testActualizarpersonaNull() {
            PerfilAEditarDto perfilDto = null;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.actualizarPersona(perfilDto, username));

            assertNotNull(username);
            assertNull(perfilDto);
            assertEquals("El perfil no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);

        }

        @Test
        public void testActualizarPersonaUsernameNull() {
            PerfilAEditarDto perfilDto = new PerfilAEditarDto(1L, "Universidad");
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.actualizarPersona(perfilDto, username));

            assertNull(username);
            assertNotNull(perfilDto);
            assertEquals("El username no puede ser null.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);

        }

        @Test
        public void testActualizarPersonaPerfilNoEncontrado() {
            PerfilAEditarDto perfilDto = new PerfilAEditarDto(99999999L, "Colegio");
            String username = "Gus123";
            when(perfilRepository.findByIdAndUsuarioUsername(perfilDto.getId(), username))
                    .thenReturn(Optional.empty());


            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> perfilService.actualizarPersona(perfilDto, username));

            assertNotNull(perfilDto);
            assertNotNull(username);
            assertEquals("No se pudo actualizar los datos. No se encontró el perfil o no tenés permisos.", resultado.getMessage());
            verify(perfilRepository, times(0)).save(any(Perfil.class));
        }

        @Test
        public void testActualizarPersona() {
            PerfilAEditarDto perfilDto = new PerfilAEditarDto(1L, "Universidad");
            String username = "Gus123";
            when(perfilRepository.findByIdAndUsuarioUsername(perfilDto.getId(), username))
                    .thenReturn(Optional.of(DataProvider.getPerfilMock()));

            perfilService.actualizarPersona(perfilDto, username);

            ArgumentCaptor<Perfil> perfilEncontrado = ArgumentCaptor.forClass(Perfil.class);
            verify(perfilRepository, times(1)).save(perfilEncontrado.capture());
            assertNotNull(perfilEncontrado.getValue());
            assertEquals(username, perfilEncontrado.getValue().getUsuario().getUsername());
            verify(perfilRepository, times(1)).findByIdAndUsuarioUsername(perfilDto.getId(), username);


        }


    }

    @Nested
    class ActualizarPersonaRestTests {
        @Test
        public void testActualizarpersonaRestNull() {
            PerfilAEditarRDto perfilDto = null;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.actualizarPersonaRest(perfilDto, username));

            assertNotNull(username);
            assertNull(perfilDto);
            assertEquals("El perfil no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);

        }

        @Test
        public void testActualizarPersonaRestUsernameNull() {
            PerfilAEditarRDto perfilDto = new PerfilAEditarRDto(1L, "Universidad");
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> perfilService.actualizarPersonaRest(perfilDto, username));

            assertNull(username);
            assertNotNull(perfilDto);
            assertEquals("El username no puede ser null.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);

        }

        @Test
        public void testActualizarPersonaRestPerfilNoEncontrado() {
            PerfilAEditarRDto perfilDto = new PerfilAEditarRDto(99999999L, "Colegio");
            String username = "Gus123";
            when(perfilRepository.findByIdAndUsuarioUsername(perfilDto.getId(), username))
                    .thenReturn(Optional.empty());


            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> perfilService.actualizarPersonaRest(perfilDto, username));

            assertNotNull(perfilDto);
            assertNotNull(username);
            assertEquals("No se pudo actualizar los datos. No se encontró el perfil o no tenés permisos.", resultado.getMessage());
            verify(perfilRepository, times(0)).save(any(Perfil.class));
        }

        @Test
        public void testActualizarPersona() {
            PerfilAEditarRDto perfilDto = new PerfilAEditarRDto(1L, "Universidad");
            String username = "Gus123";
            when(perfilRepository.findByIdAndUsuarioUsername(perfilDto.getId(), username))
                    .thenReturn(Optional.of(DataProvider.getPerfilMock()));

            perfilService.actualizarPersonaRest(perfilDto, username);

            ArgumentCaptor<Perfil> perfilEncontrado = ArgumentCaptor.forClass(Perfil.class);
            verify(perfilRepository, times(1)).save(perfilEncontrado.capture());
            assertNotNull(perfilEncontrado.getValue());
            assertEquals(username, perfilEncontrado.getValue().getUsuario().getUsername());
            verify(perfilRepository, times(1)).findByIdAndUsuarioUsername(perfilDto.getId(), username);


        }

    }


}
