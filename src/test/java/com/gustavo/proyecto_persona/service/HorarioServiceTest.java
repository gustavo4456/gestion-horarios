package com.gustavo.proyecto_persona.service;

import com.gustavo.proyecto_persona.DataProvider;
import com.gustavo.proyecto_persona.model.Horario;
import com.gustavo.proyecto_persona.repository.HorarioRepository;
import com.gustavo.proyecto_persona.repository.MateriaRepository;

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

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class HorarioServiceTest {

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private MateriaRepository materiaRepository;

    @InjectMocks
    private HorarioService horarioService;

    @Nested
    class GetHorariosTests {
        @Test
        public void testGetHorariosUsernameNull() {
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.getHorarios(username));

            assertEquals("El username no puede ser null.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGetHorariosUsernameVacio() {
            String username = "";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.getHorarios(username));

            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGetHorarios() {
            String username = "Gus123";
            when(horarioRepository.findAllByMateriaUsuarioUsernameOrderByMateriaNombreAscHoraInicioAsc(username))
                    .thenReturn(DataProvider.getHorariosMock());

            List<Horario> resultado = horarioService.getHorarios(username);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getFirst().getId());

        }
    }

    @Nested
    class GetHorariosPorAnioCursadaTests {
        @Test
        public void testGetHorariosPorAnioCursadaUsernameNull() {
            String username = null;
            Long anioCursada = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.getHorariosPorAnioCursada(anioCursada, username));


            assertEquals("El username no puede ser null.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGetHorariosPorAnioCursadaUsernameVacio() {
            String username = " ";
            Long anioCursada = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.getHorariosPorAnioCursada(anioCursada, username));


            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGetHorariosPorAnioCursadaIdNull() {
            String username = "Gus123";
            Long anioCursada = null;

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> horarioService.getHorariosPorAnioCursada(anioCursada, username));

            assertEquals("El año de cursada debe ser mayor a cero y no ser nulo.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGetHorariosPorAnioCursadaIdNegativo() {
            String username = "Gus123";
            Long anioCursada = -10L;

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> horarioService.getHorariosPorAnioCursada(anioCursada, username));

            assertEquals("El año de cursada debe ser mayor a cero y no ser nulo.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGetHorariosPorAnioCursada() {
            String username = "Gus123";
            Long anioCursada = 1L;
            when(horarioRepository.findHorariosPorAnioCursadaYUsuario(anioCursada, username))
                    .thenReturn(DataProvider.getHorariosMock());

            List<Horario> resultado = horarioService.getHorariosPorAnioCursada(anioCursada, username);

            assertNotNull(resultado);
            assertEquals(username, resultado.getFirst().getMateria().getUsuario().getUsername());
        }

    }

    @Nested
    class GetHorarioPorIdTests {
        @Test
        public void testGetHorarioPorIdUsernameNull() {
            Long id = 1L;
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.getHorarioPorId(id, username));

            assertEquals("El username no puede ser null.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGetHorarioPorIdNull() {
            Long id = null;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.getHorarioPorId(id, username));

            assertEquals("El id no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGetHorarioPorIdCero() {
            Long id = 0L;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.getHorarioPorId(id, username));

            assertEquals("El id no puede ser 0 o negativo.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGetHorarioPorIdNegativo() {
            Long id = -10L;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.getHorarioPorId(id, username));

            assertEquals("El id no puede ser 0 o negativo.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGetHorarioPorIdNoEncontrado() {
            Long id = 1L;
            String username = "Gus123";
            when(horarioRepository.findByIdAndMateriaUsuarioUsername(id, username))
                    .thenReturn(Optional.empty());

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> horarioService.getHorarioPorId(id, username));

            assertEquals("No se encontró el horario o no tienes permisos para acceder.", resultado.getMessage());
        }

        @Test
        public void testGetHorarioPorId() {
            Long id = 1L;
            String username = "Gus123";
            when(horarioRepository.findByIdAndMateriaUsuarioUsername(id, username))
                    .thenReturn(Optional.of(DataProvider.getHorarioMock()));

            Horario resultado = horarioService.getHorarioPorId(id, username);

            assertNotNull(resultado);
            assertEquals(username, resultado.getMateria().getUsuario().getUsername());
            assertEquals(1L, resultado.getId());
            assertEquals("Química I", resultado.getMateria().getNombre());
        }
    }

    @Nested
    class BorrarHorarioTests {
        @Test
        public void testBorrarHorarioUsernameNull() {
            Long id = 1L;
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.borrarHorario(id, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testBorrarHorarioIdNull() {
            Long id = null;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.borrarHorario(id, username));

            assertEquals("El id no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testBorrarHorarioIdNegativo() {
            Long id = -10L;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.borrarHorario(id, username));

            assertEquals("El id no puede ser negativo o cero.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testBorrarHorarioIdCero() {
            Long id = 0L;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.borrarHorario(id, username));

            assertEquals("El id no puede ser negativo o cero.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testBorrarHorarioNoEncontrado() {
            Long id = 999999L;
            String username = "Gus123";
            when(horarioRepository.existsByIdAndMateriaUsuarioUsername(id, username))
                    .thenReturn(false);

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> horarioService.borrarHorario(id, username));

            assertEquals("No se pudo borrar el horario: no existe o no tienes permisos.", resultado.getMessage());
            verify(horarioRepository, times(1)).existsByIdAndMateriaUsuarioUsername(id, username);
            verify(horarioRepository, times(0)).deleteById(id);

        }

        @Test
        public void testBorrarHorario() {
            Long id = 1L;
            String username = "Gus123";
            when(horarioRepository.existsByIdAndMateriaUsuarioUsername(id, username))
                    .thenReturn(true);

            horarioService.borrarHorario(id, username);

            verify(horarioRepository, times(1)).existsByIdAndMateriaUsuarioUsername(id, username);
            verify(horarioRepository, times(1)).deleteById(id);

        }
    }

    @Nested
    class GuardarHorarioTests {
        @Test
        public void testGuardarHorarioUsernameNull() {
            Horario horario = DataProvider.getHorarioSinIdMock();
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.guardarHorario(horario, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGuardarHorarioHorarioNull() {
            Horario horario = null;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.guardarHorario(horario, username));

            assertEquals("El horario no debe ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGuardarHorarioIdHorarioDifNull() {
            Horario horario = DataProvider.getHorarioMock();
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> horarioService.guardarHorario(horario, username));

            assertEquals("El id del horario debe ser null.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
            verifyNoInteractions(horarioRepository);
        }

        @Test
        public void testGuardarHorarioNoLePerteneceMateriaAUsuario() {
            Horario horario = DataProvider.getHorarioSinIdMock();
            String username = "Gus123";
            when(materiaRepository.existsByIdAndUsuarioUsername(horario.getMateria().getId(), username))
                    .thenReturn(false);

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> horarioService.guardarHorario(horario, username));

            assertEquals("No puedes asignar horarios a una materia que no te pertenece.", resultado.getMessage());
            verify(materiaRepository, times(1))
                    .existsByIdAndUsuarioUsername(horario.getMateria().getId(), username);
            verifyNoInteractions(horarioRepository);

        }

        @Test
        public void testGuardarHorario() {
            Horario horario = DataProvider.getHorarioSinIdMock();
            String username = "Gus123";
            when(materiaRepository.existsByIdAndUsuarioUsername(horario.getMateria().getId(), username))
                    .thenReturn(true);

            horarioService.guardarHorario(horario, username);

            verify(materiaRepository, times(1))
                    .existsByIdAndUsuarioUsername(horario.getMateria().getId(), username);
            verify(horarioRepository, times(1)).save(horario);

        }
    }

    @Nested
    class ActualizarHorarioTests {
        @Test
        public void testActualizarHorarioUsernameNull() {
            Horario horario = DataProvider.getHorarioMock();
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.actualizarHorario(horario, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testActualizarHorarioUsernameVacio() {
            Horario horario = DataProvider.getHorarioMock();
            String username = "   ";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.actualizarHorario(horario, username));

            assertEquals("El username no puede estar vacio.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testActualizarHorarioHorarioNull() {
            Horario horario = null;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> horarioService.actualizarHorario(horario, username));

            assertEquals("El horario no debe ser nulo.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testActualizarHorarioHorarioIdNull() {
            Horario horario = DataProvider.getHorarioSinIdMock();
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> horarioService.actualizarHorario(horario, username));

            assertEquals("Para actualizar un horario, su id no puede ser null.", resultado.getMessage());
            verifyNoInteractions(horarioRepository);
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testActualizarHorarioHorarioNoPerteneceAlUsername() {
            Horario horario = DataProvider.getHorarioMock();
            String username = "Gus123";
            when(horarioRepository.findByIdAndMateriaUsuarioUsername(horario.getId(), username))
                    .thenReturn(Optional.empty());

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> horarioService.actualizarHorario(horario, username));

            assertEquals("No se encontró el horario a actualizar o no tienes permisos.", resultado.getMessage());
            verify(horarioRepository, times(1))
                    .findByIdAndMateriaUsuarioUsername(horario.getId(), username);
            verifyNoInteractions(materiaRepository);
            verify(horarioRepository, times(0))
                    .save(horario);


        }

        @Test
        public void testActualizarHorarioMateriaNoLePerteneceAlUsername() {
            Horario horario = DataProvider.getHorarioMock();
            String username = "Gus123";
            when(horarioRepository.findByIdAndMateriaUsuarioUsername(horario.getId(), username))
                    .thenReturn(Optional.of(DataProvider.getHorarioMock()));
            when(materiaRepository.existsByIdAndUsuarioUsername(horario.getMateria().getId(), username))
                    .thenReturn(false);

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> horarioService.actualizarHorario(horario, username));


            assertEquals("No puedes transferir este horario a una materia que no te pertenece.", resultado.getMessage());
            verify(horarioRepository, times(1))
                    .findByIdAndMateriaUsuarioUsername(horario.getId(), username);
            verify(materiaRepository, times(1))
                    .existsByIdAndUsuarioUsername(horario.getMateria().getId(), username);
            verify(horarioRepository, times(0))
                    .save(horario);
        }

        @Test
        public void testActualizarHorario() {
            Horario horario = DataProvider.getHorarioMock();
            String username = "Gus123";
            when(horarioRepository.findByIdAndMateriaUsuarioUsername(horario.getId(), username))
                    .thenReturn(Optional.of(DataProvider.getHorarioMock()));
            when(materiaRepository.existsByIdAndUsuarioUsername(horario.getMateria().getId(), username))
                    .thenReturn(true);

            horarioService.actualizarHorario(horario, username);

            ArgumentCaptor<Horario> horarioEncontrado = ArgumentCaptor.forClass(Horario.class);
            verify(horarioRepository, times(1))
                    .findByIdAndMateriaUsuarioUsername(horario.getId(), username);
            verify(materiaRepository, times(1))
                    .existsByIdAndUsuarioUsername(horario.getMateria().getId(), username);
            verify(horarioRepository, times(1))
                    .save(horarioEncontrado.capture());
        }
    }
}
