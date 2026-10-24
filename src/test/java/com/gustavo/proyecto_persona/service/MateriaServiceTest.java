package com.gustavo.proyecto_persona.service;

import com.gustavo.proyecto_persona.DataProvider;
import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.model.Usuario;
import com.gustavo.proyecto_persona.repository.MateriaRepository;
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
public class MateriaServiceTest {

    @Mock
    private MateriaRepository materiaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MateriaService materiaService;

    @Nested
    class GetMateriasTests {
        @Test
        public void testGetMateriasUsernameNull() {
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.getMaterias(username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGetMateriasUsernameVacio() {
            String username = "     ";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.getMaterias(username));

            assertEquals("El username no puede estar vacio o con espacios en blanco.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGetMaterias() {
            String username = "Gus123";
            when(materiaRepository.findByUsuarioUsername(username))
                    .thenReturn(DataProvider.getMateriasMock());

            List<Materia> resultado = materiaService.getMaterias(username);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getFirst().getId());
            assertEquals("Química", resultado.getFirst().getNombre());
            assertEquals(username, resultado.getFirst().getUsuario().getUsername());
            verify(materiaRepository).findByUsuarioUsername(username);
        }
    }

    @Nested
    class GetMateriaPorIdTests {
        @Test
        public void testGetMateriaPorIdUsernameNull() {
            String username = null;
            Long id = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.getMateriaPorId(id, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGetMateriaUsernameVacio() {
            String username = "     ";
            Long id = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.getMateriaPorId(id, username));

            assertEquals("El username no puede estar vacio o con espacios en blanco.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGetMateriaPorIdNull() {
            String username = "Gus123";
            Long id = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.getMateriaPorId(id, username));

            assertEquals("El id no puede ser null.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGetMateriaPorIdNegativo() {
            String username = "Gus123";
            Long id = -1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.getMateriaPorId(id, username));

            assertEquals("El id no puede ser negativo o cero.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGetMateriaPorIdCero() {
            String username = "Gus123";
            Long id = 0L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.getMateriaPorId(id, username));

            assertEquals("El id no puede ser negativo o cero.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGetMateriaPorIdNoEncontado() {
            String username = "Gus123";
            Long id = 9999999L;
            when(materiaRepository.findByIdAndUsuarioUsername(id, username))
                    .thenReturn(Optional.empty());

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.getMateriaPorId(id, username));

            assertEquals("No se encontró la materia o no tienes permisos para verla.", resultado.getMessage());

        }

        @Test
        public void testGetMateriaPorId() {
            String username = "Gus123";
            Long id = 1L;
            when(materiaRepository.findByIdAndUsuarioUsername(id, username))
                    .thenReturn(Optional.of(DataProvider.getMateriaMock()));

            Materia resultado = materiaService.getMateriaPorId(id, username);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals("Química", resultado.getNombre());
            assertEquals(username, resultado.getUsuario().getUsername());
            verify(materiaRepository).findByIdAndUsuarioUsername(id, username);
        }
    }

    @Nested
    class BorrarMateriaTests {
        @Test
        public void testGetBorrarUsernameNull() {
            String username = null;
            Long id = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.borrarMateria(id, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testBorrarMateriaUsernameVacio() {
            String username = "     ";
            Long id = 1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.borrarMateria(id, username));

            assertEquals("El username no puede estar vacio o con espacios en blanco.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testBorrarMateriaIdNull() {
            String username = "Gus123";
            Long id = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.borrarMateria(id, username));

            assertEquals("El id no puede ser null.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testBorrarMateriaIdNegativo() {
            String username = "Gus123";
            Long id = -1L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.borrarMateria(id, username));

            assertEquals("El id no puede ser negativo o cero.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testBorrarMateriaIdCero() {
            String username = "Gus123";
            Long id = 0L;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.borrarMateria(id, username));

            assertEquals("El id no puede ser negativo o cero.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testBorrarMateriaNoEncontrada() {
            String username = "Gus123";
            Long id = 99999999999999L;
            when(materiaRepository.existsByIdAndUsuarioUsername(id, username))
                    .thenReturn(false);

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.borrarMateria(id, username));

            assertEquals("No se pudo eliminar la materia: no existe o no tienes permisos.", resultado.getMessage());
            verify(materiaRepository, times(0)).deleteById(id);
        }

        @Test
        public void testBorrarMateria() {
            String username = "Gus123";
            Long id = 1L;
            when(materiaRepository.existsByIdAndUsuarioUsername(id, username))
                    .thenReturn(true);

            materiaService.borrarMateria(id, username);

            verify(materiaRepository, times(1)).existsByIdAndUsuarioUsername(id, username);
            verify(materiaRepository, times(1)).deleteById(id);

        }

    }

    @Nested
    class GuardarMateriaTests {
        @Test
        public void testGuardarMateriaUsernameNull() {
            String username = null;
            Materia materia = DataProvider.getMateriaSinIdMock();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.guardarMateria(materia, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGuardarMateriaUsernameVacio() {
            String username = "     ";
            Materia materia = DataProvider.getMateriaSinIdMock();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.guardarMateria(materia, username));

            assertEquals("El username no puede estar vacio o con espacios en blanco.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGuardarMateriaNull() {
            String username = "Gus123";
            Materia materia = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.guardarMateria(materia, username));

            assertEquals("La Materia no puede ser null.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGuardarMateriaConId() {
            String username = "Gus123";
            Materia materia = DataProvider.getMateriaMock();

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.guardarMateria(materia, username));

            assertEquals("Para poder crear una materia el id debe ser null.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGuardarMateriaUsuarioNoEncontrada() {
            String username = "Gus123";
            Materia materia = DataProvider.getMateriaSinIdMock();
            when(usuarioRepository.findByUsername(username))
                    .thenReturn(Optional.empty());

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.guardarMateria(materia, username));

            assertEquals("No se encontró el usuario logueado en la base de datos.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testGuardarMateria() {
            String username = "Gus123";
            Materia materia = DataProvider.getMateriaSinIdMock();
            when(usuarioRepository.findByUsername(username))
                    .thenReturn(Optional.of(DataProvider.getUsuarioMock()));

            materiaService.guardarMateria(materia, username);

            verify(usuarioRepository).findByUsername(username);
            verify(materiaRepository).save(materia);
        }

    }

    @Nested
    class ActualizarMateriaTests {
        @Test
        public void testActualizarMateriaUsernameNull() {
            String username = null;
            Materia materia = DataProvider.getMateriaSinIdMock();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.actualizarMateria(materia, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testActualizarMateriaUsernameVacio() {
            String username = "     ";
            Materia materia = DataProvider.getMateriaSinIdMock();

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.actualizarMateria(materia, username));

            assertEquals("El username no puede estar vacio o con espacios en blanco.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testActualizarMateriaNull() {
            String username = "Gus123";
            Materia materia = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.actualizarMateria(materia, username));

            assertEquals("La Materia no puede ser null.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testActualizarMateriaConId() {
            String username = "Gus123";
            Materia materia = DataProvider.getMateriaSinIdMock();

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.actualizarMateria(materia, username));

            assertEquals("El id de la materia no puede ser null para actualizar sus datos.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);
        }

        @Test
        public void testActualizarMateriaNoEncontrada() {
            String username = "Gus123";
            Materia materia = DataProvider.getMateriaMock();
            when(materiaRepository.findByIdAndUsuarioUsername(materia.getId(), username))
                    .thenReturn(Optional.empty());

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.actualizarMateria(materia, username));

            assertEquals("No se encontró la materia a editar o no te pertenece.", resultado.getMessage());
            ArgumentCaptor<Materia> materiaBD = ArgumentCaptor.forClass(Materia.class);
            verify(materiaRepository, times(0)).save(materiaBD.capture());
        }

        @Test
        public void testActualizarMateria() {
            String username = "Gus123";
            Materia materia = DataProvider.getMateriaMock();
            when(materiaRepository.findByIdAndUsuarioUsername(materia.getId(), username))
                    .thenReturn(Optional.of(DataProvider.getMateriaMock()));

            materiaService.actualizarMateria(materia, username);

            ArgumentCaptor<Materia> materiaBD = ArgumentCaptor.forClass(Materia.class);
            verify(materiaRepository, times(1)).findByIdAndUsuarioUsername(materia.getId(), username);
            verify(materiaRepository, times(1)).save(materiaBD.capture());
        }
    }

    @Nested
    class BuscarPorNombreTests {
        @Test
        public void testBuscarPorNombreUsernameNull() {
            String nombre = "Química";
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.buscarPorNombre(nombre, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombreUsernameVacio() {
            String nombre = "Química";
            String username = " ";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.buscarPorNombre(nombre, username));

            assertEquals("El username no puede estar vacio o con espacios en blanco.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombreNull() {
            String nombre = null;
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.buscarPorNombre(nombre, username));

            assertEquals("El nombre de la materia no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombreVacio() {
            String nombre = " ";
            String username = "Gus123";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.buscarPorNombre(nombre, username));

            assertEquals("El nombre de la materia no puede estar vacío o con espacios en blanco.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombre() {
            String nombre = "Quí";
            String username = "Gus123";
            when(materiaRepository.findByNombreContainingIgnoreCaseAndUsuarioUsername(nombre, username))
                    .thenReturn(DataProvider.getMateriasPorNombreMock());

            List<Materia> resultado = materiaService.buscarPorNombre(nombre, username);

            assertNotNull(resultado);
            for (Materia mat : resultado) {
                assertTrue(mat.getNombre().contains(nombre));
            }
        }
    }

    @Nested
    class BuscarPorAnioCursadaTests {
        @Test
        public void testBuscarPorAnioCursadaUsernameNull() {
            Integer anioCursada = 1;
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.buscarPorAnioCursada(anioCursada, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorAnioCursadaUsernameVacio() {
            Integer anioCursada = 1;
            String username = " ";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.buscarPorAnioCursada(anioCursada, username));

            assertEquals("El username no puede estar vacio o con espacios en blanco.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorAnioCursadaNull() {
            Integer anioCursada = null;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.buscarPorAnioCursada(anioCursada, username));

            assertEquals("El año lectivo no debe ser null.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorAnioCursadaNegativo() {
            Integer anioCursada = -1;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.buscarPorAnioCursada(anioCursada, username));

            assertEquals("El año lectivo debe ser mayor a 0.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorAnioCursadaCero() {
            Integer anioCursada = 0;
            String username = "Gus123";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.buscarPorAnioCursada(anioCursada, username));

            assertEquals("El año lectivo debe ser mayor a 0.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorAnioCursada() {
            Integer anioCursada = 2;
            String username = "Gus123";
            when(materiaRepository.findByAnioCursadaAndUsuarioUsername(anioCursada, username))
                    .thenReturn(DataProvider.getMateriasPorAnioCursadaMock());

            List<Materia> resultado = materiaService.buscarPorAnioCursada(anioCursada, username);

            assertNotNull(resultado);

            for (Materia mat : resultado) {
                assertEquals(anioCursada, mat.getAnioCursada());
            }
        }
    }

    @Nested
    class BuscarPorNombreYAnioCursadaTests {

        @Test
        public void testBuscarPorNombreNull() {
            String nombre = null;
            String username = "Gus123";
            Integer anioCursada = 2;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.buscarPorNombreYAanioCursada(nombre,anioCursada, username));

            assertEquals("El nombre de la materia no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombreVacio() {
            String nombre = " ";
            String username = "Gus123";
            Integer anioCursada = 2;


            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.buscarPorNombreYAanioCursada(nombre, anioCursada, username));

            assertEquals("El nombre de la materia no puede estar vacío o con espacios en blanco.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombreYAnioCursadaUsernameNull() {
            String nombre = "Quí";
            Integer anioCursada = 1;
            String username = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.buscarPorNombreYAanioCursada(nombre, anioCursada, username));

            assertEquals("El username no puede ser nulo.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombreYAnioCursadaUsernameVacio() {
            String nombre = "Quí";
            Integer anioCursada = 1;
            String username = " ";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,
                    () -> materiaService.buscarPorNombreYAanioCursada(nombre, anioCursada, username));

            assertEquals("El username no puede estar vacio o con espacios en blanco.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombreYAnioCursadaNull() {
            Integer anioCursada = null;
            String username = "Gus123";
            String nombre = "Quí";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.buscarPorNombreYAanioCursada(nombre, anioCursada, username));

            assertEquals("El año lectivo no debe ser null.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombreYAnioCursadaNegativo() {
            Integer anioCursada = -1;
            String username = "Gus123";
            String nombre = "Quí";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.buscarPorNombreYAanioCursada(nombre, anioCursada, username));

            assertEquals("El año lectivo debe ser mayor a 0.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombreYAnioCursadaCero() {
            Integer anioCursada = 0;
            String username = "Gus123";
            String nombre = "Quí";

            EntityNotFoundException resultado = assertThrows(EntityNotFoundException.class,
                    () -> materiaService.buscarPorNombreYAanioCursada(nombre, anioCursada, username));

            assertEquals("El año lectivo debe ser mayor a 0.", resultado.getMessage());
            verifyNoInteractions(materiaRepository);

        }

        @Test
        public void testBuscarPorNombreYAnio() {
            Integer anioCursada = 1;
            String username = "Gus123";
            String nombre = "Quí";
            when(materiaRepository.findByNombreContainingIgnoreCaseAndAnioCursadaAndUsuarioUsername(nombre, anioCursada, username))
                    .thenReturn(DataProvider.getMateriasPorNombreYanioMateriaMock());

            List<Materia> resultado = materiaService.buscarPorNombreYAanioCursada(nombre, anioCursada, username);

            assertNotNull(resultado);

            for (Materia mat : resultado) {
                assertEquals(username, mat.getUsuario().getUsername());
                assertTrue(mat.getNombre().contains(nombre));
                assertEquals(anioCursada, mat.getAnioCursada());
            }
        }


    }
}
