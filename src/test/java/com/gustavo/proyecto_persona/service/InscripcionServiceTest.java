package com.gustavo.proyecto_persona.service;

import com.gustavo.proyecto_persona.DataProvider;
import com.gustavo.proyecto_persona.model.Inscripcion;
import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.repository.InscripcionRepository;
import com.gustavo.proyecto_persona.repository.PerfilRepository;

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
import java.util.stream.LongStream;

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
    class BorrarInscripcionTests {
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

    @Nested
    class GuardarInscripcionTests {
        @Test
        public void testGuardarInscripcionUsernameNull() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = null;
            List<Long> seleccion = LongStream.range(1, 11).boxed().toList();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.guardarInscripcion(inscripcion,seleccion, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGuardarInscripcionUsernameVacio() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = "";
            List<Long> seleccion = LongStream.range(1, 11).boxed().toList();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.guardarInscripcion(inscripcion,seleccion, username));

            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testGuardarInscripcionInscripcionNull() {
            Inscripcion inscripcion = null;
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1, 11).boxed().toList();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.guardarInscripcion(inscripcion,seleccion, username));

            assertEquals("La inscripcion no puede ser null.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testGuardarInscripcionInscripcionIdNull() {
            Inscripcion inscripcion = DataProvider.getInscripcionMock();
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1, 11).boxed().toList();

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.guardarInscripcion(inscripcion,seleccion, username));

            assertEquals("Para guardar una inscripción el id debe ser null.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testGuardarInscripcionSeleccionNull() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = "Gus123";
            List<Long> seleccion = null;

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.guardarInscripcion(inscripcion,seleccion, username));

            assertEquals("No seleccionó materias.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testGuardarInscripcionSeleccionVacio() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = "Gus123";
            List<Long> seleccion = List.of();

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.guardarInscripcion(inscripcion,seleccion, username));

            assertEquals("No seleccionó materias.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testGuardarInscripcionPerfilNoPerteneceAlUsuario() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = "Gus1234";
            List<Long> seleccion = LongStream.range(1,11).boxed().toList();
            when(perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(), username))
                    .thenReturn(false);

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.guardarInscripcion(inscripcion,seleccion, username));

            assertEquals("La persona seleccionada no pertenece a tu usuario.", resultado.getMessage());
            verify(perfilRepository, times(1)).existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(), username);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testGuardarInscripcionYaExisteElAniooParaEstaCuenta() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1,11).boxed().toList();
            when(perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(), username))
                    .thenReturn(true);
            when(inscripcionRepository.existsByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(inscripcion.getPerfil().getId(),
                    inscripcion.getAnioLectivo(),
                    username))
                    .thenReturn(true);

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.guardarInscripcion(inscripcion,seleccion, username));

            assertEquals("La persona ya tiene una inscripción para ese año lectivo.", resultado.getMessage());
            verify(perfilRepository, times(1)).existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(), username);
            verify(inscripcionRepository, times(1))
                    .existsByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(inscripcion.getPerfil().getId(),
                            inscripcion.getAnioLectivo(),
                            username);
            verify(inscripcionRepository, times(0)).save(inscripcion);
            verifyNoInteractions(materiaService);

        }

        @Test
        public void testGuardarInscripcionAlgunaMateriaNoPerteneceAlUsuario() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1,11).boxed().toList();
            when(perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(), username))
                    .thenReturn(true);
            when(inscripcionRepository.existsByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(inscripcion.getPerfil().getId(),
                    inscripcion.getAnioLectivo(),
                    username))
                    .thenReturn(false);
            when(inscripcionRepository.save(inscripcion))
                    .thenReturn(DataProvider.getInscripcionMock());
            when(materiaService.getMateriaPorId(1L,"Gus123"))
                    .thenThrow(new EntityNotFoundException("No se encontró la materia o no tienes permisos para verla."));

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.guardarInscripcion(inscripcion,seleccion, username));

            assertEquals("No se encontró la materia o no tienes permisos para verla.", resultado.getMessage());
            verify(perfilRepository, times(1))
                    .existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(), username);
            verify(materiaService, times(1))
                    .getMateriaPorId(1L, "Gus123");
            verify(inscripcionRepository, times(1))
                    .existsByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(inscripcion.getPerfil().getId(),
                            inscripcion.getAnioLectivo(),
                            username);
            verify(inscripcionRepository, times(1)).save(inscripcion);

        }

        @Test
        public void testGuardarInscripcion() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1,11).boxed().toList();
            when(perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(), username))
                    .thenReturn(true);
            when(inscripcionRepository.existsByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(inscripcion.getPerfil().getId(),
                    inscripcion.getAnioLectivo(),
                    username))
                    .thenReturn(false);
            when(inscripcionRepository.save(inscripcion))
                    .thenReturn(DataProvider.getInscripcionMock());

            for (Long sel : seleccion){
                when(materiaService.getMateriaPorId(sel,"Gus123"))
                        .thenReturn(DataProvider.getMateriaMock());
            }


           inscripcionService.guardarInscripcion(inscripcion,seleccion, username);

            ArgumentCaptor<Inscripcion> inscripcionGuardadCap = ArgumentCaptor.forClass(Inscripcion.class);
            verify(perfilRepository, times(1))
                    .existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(), username);

            for (Long sel : seleccion) {
                verify(materiaService)
                        .getMateriaPorId(sel, "Gus123");
            }

            verify(inscripcionRepository, times(1))
                    .existsByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(inscripcion.getPerfil().getId(),
                            inscripcion.getAnioLectivo(),
                            username);

            verify(inscripcionRepository, times(2)).save(inscripcionGuardadCap.capture());

            assertNotNull(inscripcionGuardadCap);
            assertEquals(2026, inscripcionGuardadCap.getValue().getAnioLectivo());
        }
    }

    @Nested
    class ActualizarInscripcionTests{

        @Test
        public void testActualizarInscripcionUsernameNull() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = null;
            List<Long> seleccion = LongStream.range(1, 11).boxed().toList();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.actualizarInscripcion(inscripcion,seleccion, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testActualizarInscripcionUsernameVacio() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = "";
            List<Long> seleccion = LongStream.range(1, 11).boxed().toList();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.actualizarInscripcion(inscripcion,seleccion, username));

            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testActualizarInscripcionInscripcionNull() {
            Inscripcion inscripcion = null;
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1, 11).boxed().toList();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.actualizarInscripcion(inscripcion,seleccion, username));

            assertEquals("La inscripcion no puede ser null.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testActualizarInscripcionInscripcionIdNull() {
            Inscripcion inscripcion = DataProvider.getInscripcionSinIdMock();
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1, 11).boxed().toList();

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.actualizarInscripcion(inscripcion,seleccion, username));

            assertEquals("Para actualizar datos de una inscripción el id no debe ser nulo.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verifyNoInteractions(inscripcionRepository);

        }

        @Test
        public void testActualizarInscripcionNoPerteneceAlUsuario() {
            Inscripcion inscripcion = DataProvider.getInscripcionMock();
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1,11).boxed().toList();
            when(inscripcionRepository.findByIdAndPerfilUsuarioUsername(inscripcion.getId(), username))
                    .thenReturn(Optional.empty());

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.actualizarInscripcion(inscripcion,seleccion, username));

            assertEquals("No se encontró la inscripción a editar o no tienes permisos.", resultado.getMessage());
            verifyNoInteractions(perfilRepository);
            verifyNoInteractions(materiaService);
            verify(inscripcionRepository, times(0)).save(inscripcion);

        }

        @Test
        public void testActualizarInscripcionPerfilNoPerteneceAlUsuario() {
            Inscripcion inscripcion = DataProvider.getInscripcionMock();
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1,11).boxed().toList();
            when(inscripcionRepository.findByIdAndPerfilUsuarioUsername(inscripcion.getId(), username))
                    .thenReturn(Optional.of(DataProvider.getInscripcionMock()));
            when(perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(),
                    username))
                    .thenReturn(false);


            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.actualizarInscripcion(inscripcion,seleccion, username));

            assertEquals("La persona seleccionada no pertenece a tu usuario.", resultado.getMessage());
            verifyNoInteractions(materiaService);
            verify(inscripcionRepository, times(0)).save(inscripcion);

        }

        @Test
        public void testActualizarInscripcionMateriaNoPerteneceAIncripcion() {
            Inscripcion inscripcion = DataProvider.getInscripcionMock();
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1,11).boxed().toList();
            when(inscripcionRepository.findByIdAndPerfilUsuarioUsername(inscripcion.getId(), username))
                    .thenReturn(Optional.of(DataProvider.getInscripcionMock()));
            when(perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(),
                    username))
                    .thenReturn(true);


            when(materiaService.getMateriaPorId(1L, username))
                    .thenThrow(new EntityNotFoundException("No se encontró la materia o no tienes permisos para verla."));




            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.actualizarInscripcion(inscripcion,seleccion, username));

            assertEquals("No se encontró la materia o no tienes permisos para verla.", resultado.getMessage());

            verify(materiaService, times(1)).getMateriaPorId(1L, username);

            verify(inscripcionRepository, times(0)).save(any(Inscripcion.class));

        }


        @Test
        public void testActualizarSeleccionNull() {
            Inscripcion inscripcion = DataProvider.getInscripcionMock();
            String username = "Gus123";
            List<Long> seleccion = null;
            when(inscripcionRepository.findByIdAndPerfilUsuarioUsername(inscripcion.getId(), username))
                    .thenReturn(Optional.of(DataProvider.getInscripcionMock()));
            when(perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(),
                    username))
                    .thenReturn(true);


            inscripcionService.actualizarInscripcion(inscripcion,seleccion, username);


            ArgumentCaptor<Inscripcion>inscripcionArgumentCaptor = ArgumentCaptor.forClass(Inscripcion.class);
            verifyNoInteractions(materiaService);

            verify(inscripcionRepository, times(1))
                    .save(inscripcionArgumentCaptor.capture());

            assertNotNull(inscripcionArgumentCaptor);
            assertEquals(2026, inscripcionArgumentCaptor.getValue().getAnioLectivo());

            for (Materia matOriginal : inscripcion.getMaterias()){
                for (Materia matMod : inscripcionArgumentCaptor.getValue().getMaterias()){
                    assertEquals(matOriginal.getId(), matMod.getId());
                }
            }
        }

        @Test
        public void testActualizarSeleccionVacia() {
            Inscripcion inscripcion = DataProvider.getInscripcionMock();
            String username = "Gus123";
            List<Long> seleccion = List.of();
            when(inscripcionRepository.findByIdAndPerfilUsuarioUsername(inscripcion.getId(), username))
                    .thenReturn(Optional.of(DataProvider.getInscripcionMock()));
            when(perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(),
                    username))
                    .thenReturn(true);


            inscripcionService.actualizarInscripcion(inscripcion,seleccion, username);


            ArgumentCaptor<Inscripcion>inscripcionArgumentCaptor = ArgumentCaptor.forClass(Inscripcion.class);
            verifyNoInteractions(materiaService);

            verify(inscripcionRepository, times(1))
                    .save(inscripcionArgumentCaptor.capture());

            assertNotNull(inscripcionArgumentCaptor);
            assertEquals(2026, inscripcionArgumentCaptor.getValue().getAnioLectivo());

            for (Materia matOriginal : inscripcion.getMaterias()){
                for (Materia matMod : inscripcionArgumentCaptor.getValue().getMaterias()){
                    assertEquals(matOriginal.getId(), matMod.getId());
                }
            }
        }

        @Test
        public void testActualizar() {
            Inscripcion inscripcion = DataProvider.getInscripcionMock();
            String username = "Gus123";
            List<Long> seleccion = LongStream.range(1,11).boxed().toList();
            when(inscripcionRepository.findByIdAndPerfilUsuarioUsername(inscripcion.getId(), username))
                    .thenReturn(Optional.of(DataProvider.getInscripcionMock()));
            when(perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(),
                    username))
                    .thenReturn(true);

            for (Long sel : seleccion){
                when(materiaService.getMateriaPorId(sel, username))
                        .thenReturn(DataProvider.getMateriaMock());
            }


            inscripcionService.actualizarInscripcion(inscripcion,seleccion, username);


            ArgumentCaptor<Inscripcion>inscripcionArgumentCaptor = ArgumentCaptor.forClass(Inscripcion.class);
            verify(materiaService, times(1)).getMateriaPorId(1L, username);

            verify(inscripcionRepository, times(1))
                    .save(inscripcionArgumentCaptor.capture());

            assertNotNull(inscripcionArgumentCaptor);
            assertEquals(2026, inscripcionArgumentCaptor.getValue().getAnioLectivo());

        }

       

    }

    @Nested
    class GetInscripcionesPorPersonasTests {
        @Test
        public void testGetInscripcionesUsernameNull() {
            Long id = 1L;
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionesPorPersona(id, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesUsernameVacio() {
            Long id = 1L;
            String username = "";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionesPorPersona(id, username));

            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesIdPersonaNegativo() {
            Long id = -9991L;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.getInscripcionesPorPersona(id, username));

            assertEquals("Debe seleccionar una persona.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesIdPersonaCero() {
            Long id = 0L;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.getInscripcionesPorPersona(id, username));

            assertEquals("Debe seleccionar una persona.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripciones() {
            Long idPerfil = 99999L;
            String username = "Gus123";
            when(inscripcionRepository.findByPerfilIdAndPerfilUsuarioUsername(idPerfil, username))
                    .thenReturn(DataProvider.getInscripcionesPorIdPerfilMock());

            List<Inscripcion> resultado = inscripcionService.getInscripcionesPorPersona(idPerfil, username);

            assertNotNull(resultado);

            for (Inscripcion ins : resultado) {
                assertEquals(idPerfil, ins.getPerfil().getId());

            }
        }
    }

    @Nested
    class GetInscripcionesPorAnioLectivoTests {
        @Test
        public void testGetInscripcionesPorAnioLectivoUsernameNull() {
            Integer anioLectivo = 2026;
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionesPorAnioLectivo(anioLectivo, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesPorAnioLectivoUsernameVacio() {
            Integer anioLectivo = 2026;
            String username = "";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionesPorAnioLectivo(anioLectivo, username));

            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesPorAnioLectivoNegativo() {
            Integer anioLectivo = -9991;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.getInscripcionesPorAnioLectivo(anioLectivo, username));

            assertEquals("Debe ingresar un año lectivo positivo.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesPorAnioLectivoCero() {
            Integer anioLectivo = 0;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.getInscripcionesPorAnioLectivo(anioLectivo, username));

            assertEquals("Debe ingresar un año lectivo positivo.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesPorAnioLectivo() {
            Integer anioLectivo = 2026;
            String username = "Gus123";
            when(inscripcionRepository.findByAnioLectivoAndPerfilUsuarioUsername(anioLectivo, username))
                    .thenReturn(DataProvider.getInscripcionesPorAnioLectivoMock());

            List<Inscripcion> resultado = inscripcionService.getInscripcionesPorAnioLectivo(anioLectivo, username);

            assertNotNull(resultado);

          boolean todosLosAniosLectivosSonIguales =  resultado.stream()
                    .allMatch(i -> i.getAnioLectivo().equals(anioLectivo));

          long cantidadUnicosPorPerfil = resultado.stream()
                  .map(i -> i.getPerfil().getId())
                  .distinct()
                  .count();

          boolean sonDistintos = resultado.size() == cantidadUnicosPorPerfil;

          assertTrue(todosLosAniosLectivosSonIguales);
          assertTrue(sonDistintos);
        }
    }

    @Nested
    class GetInscripcionesPorPersonaYAnioLectivoTests {
        @Test
        public void testGetInscripcionesPorPersonaYAnioLectivoUsernameNull() {
            Long idPerfil = 1L;
            Integer anioLectivo = 2026;
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionesPorPersonaYAnioLectivo(idPerfil, anioLectivo, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesPorPersonaYAnioLectivoUsernameVacio() {
            Long idPerfil = 1L;
            Integer anioLectivo = 2026;
            String username = "";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> inscripcionService.getInscripcionesPorPersonaYAnioLectivo(idPerfil, anioLectivo, username));

            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesPorPersonaYAnioLectivoNegativo() {
            Long idPerfil = 1L;
            Integer anioLectivo = -9991;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.getInscripcionesPorPersonaYAnioLectivo(idPerfil, anioLectivo, username));

            assertEquals("Debe ingresar un año lectivo positivo y seleccionar una persona.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesPorPersonaYAnioLectivoCero() {
            Long idPerfil = 1L;
            Integer anioLectivo = 0;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.getInscripcionesPorPersonaYAnioLectivo(idPerfil, anioLectivo, username));

            assertEquals("Debe ingresar un año lectivo positivo y seleccionar una persona.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesPorPersonaYAnioLectivoIdPerfilNegativo() {
            Long idPerfil = -999L;
            Integer anioLectivo = 2026;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.getInscripcionesPorPersonaYAnioLectivo(idPerfil, anioLectivo, username));

            assertEquals("Debe ingresar un año lectivo positivo y seleccionar una persona.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesPorPersonaYAnioLectivoIdPerfilCero() {
            Long idPerfil = 0L;
            Integer anioLectivo = 2026;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> inscripcionService.getInscripcionesPorPersonaYAnioLectivo(idPerfil, anioLectivo, username));

            assertEquals("Debe ingresar un año lectivo positivo y seleccionar una persona.", resultado.getMessage());
            verifyNoInteractions(inscripcionRepository);
        }

        @Test
        public void testGetInscripcionesPorPersonaYAnioLectivo() {
            Long idPerfil = 1L;
            Integer anioLectivo = 2026;
            String username = "Gus123";
            when(inscripcionRepository.findByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(idPerfil, anioLectivo, username))
                    .thenReturn(DataProvider.getInscripcionesPorAnioLectivoYPorIdPerfilMock());

            List<Inscripcion> resultado = inscripcionService.getInscripcionesPorPersonaYAnioLectivo(idPerfil, anioLectivo, username);

            assertNotNull(resultado);
            boolean aniosLectivosIguales = resultado.stream()
                    .allMatch(i->i.getAnioLectivo().equals(anioLectivo));
            boolean idPerfilIguales = resultado.stream()
                    .allMatch(i->i.getPerfil().getId().equals(idPerfil));
            boolean usernameIguales = resultado.stream()
                    .allMatch(i->i.getPerfil().getUsuario().getUsername().equals(username));

            assertTrue(aniosLectivosIguales);
            assertTrue(idPerfilIguales);
            assertTrue(usernameIguales);
        }

    }


}
