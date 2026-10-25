package com.gustavo.proyecto_persona.service;

import com.gustavo.proyecto_persona.DataProvider;
import com.gustavo.proyecto_persona.model.Inscripcion;
import com.gustavo.proyecto_persona.repository.InscripcionRepository;
import com.gustavo.proyecto_persona.repository.PerfilRepository;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private MateriaService materiaService;

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private InscripcionService inscripcionService;

    @Nested
    class GetInscripcionesTests {

        @Test
        public void testGetInscripcionesUsernameNull() {
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripciones(username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesUsernameVacio() {
            String username = "";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripciones(username));

            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testGetInscripciones() {
            String username = "Gus123";
            when(inscripcionRepository.findAllByPerfilUsuarioUsername(username))
                    .thenReturn(DataProvider.getInscripcionesMock());

            List<Inscripcion> resultado = inscripcionService.getInscripciones(username);

            assertNotNull(resultado);
            verify(inscripcionRepository).findAllByPerfilUsuarioUsername(username);
            assertEquals(username, resultado.getFirst().getPerfil().getUsuario().getUsername());
        }
    }

    @Nested
    class GetInscripcionPorIdTests {
        @Test
        public void testGetInscripcionPorIdUsernameNull() {
            String username = null;
            Long id = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionPorId(id, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionPorIdUsernameVacio() {
            String username = "";
            Long id = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionPorId(id, username));

            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testGetInscripcionPorIdNull() {
            String username = "Gus123";
            Long id = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionPorId(id, username));

            assertEquals("El id no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionPorIdNegativo() {
            String username = "Gus123";
            Long id = -1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionPorId(id, username));

            assertEquals("El id no puede ser Negativo o cero.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testGetInscripcionPorIdCero() {
            String username = "Gus123";
            Long id = 0L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionPorId(id, username));

            assertEquals("El id no puede ser Negativo o cero.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testGetInscripcionPorIdInscripcionNoEncontrada() {
            String username = "Gus123";
            Long id = 999999L;
            when(inscripcionRepository.findByIdAndPerfilUsuarioUsername(id, username))
                    .thenReturn(Optional.empty());

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.getInscripcionPorId(id, username));

            assertNotNull(resultado);
            assertEquals("No se encontró la inscripción o no tienes permisos para verla.", resultado.getMessage());


        }

        @Test
        public void testGetInscripcionPorId() {
            String username = "Gus123";
            Long id = 1L;
            when(inscripcionRepository.findByIdAndPerfilUsuarioUsername(id, username))
                    .thenReturn(Optional.of(DataProvider.getInscripcionMock()));

            Inscripcion resultado = inscripcionService.getInscripcionPorId(id, username);

            assertNotNull(resultado);
            assertEquals(username, resultado.getPerfil().getUsuario().getUsername());
            assertEquals(2026, resultado.getAnioLectivo());

        }
    }

    @Nested
    class BorrarInscripcionTest {
        @Test
        public void testBorrarInscripcionUsernameNull() {
            String username = null;
            Long id = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.borrarInscripcion(id, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testBorrarInscripcionUsernameVacio() {
            String username = "";
            Long id = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.borrarInscripcion(id, username));

            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testBorrarInscripcionIdNull() {
            String username = "Gus123";
            Long id = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.borrarInscripcion(id, username));

            assertEquals("El id no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testBorrarInscripcionIdNegativo() {
            String username = "Gus123";
            Long id = -1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.borrarInscripcion(id, username));

            assertEquals("El id no puede ser Negativo o cero.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testBorrarInscripcionIdCero() {
            String username = "Gus123";
            Long id = 0L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.borrarInscripcion(id, username));

            assertEquals("El id no puede ser Negativo o cero.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testBorrarInscripcionNoSeEncontro() {
            Long id = 99999L;
            String username = "Gus123";
            when(inscripcionRepository.existsByIdAndPerfilUsuarioUsername(id, username))
                    .thenReturn(false);

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.borrarInscripcion(id, username));

            assertEquals("No se encontró la inscripción a borrar o no tienes permisos.", resultado.getMessage());
            verify(inscripcionRepository, times(0)).deleteById(id);
        }

        @Test
        public void testBorrarInscripcion() {
            Long id = 1L;
            String username = "Gus123";
            when(inscripcionRepository.existsByIdAndPerfilUsuarioUsername(id, username))
                    .thenReturn(true);

            inscripcionService.borrarInscripcion(id, username);


            verify(inscripcionRepository).deleteById(id);
        }
    }

}
