package com.gustavo.proyecto_persona;

import com.gustavo.proyecto_persona.enums.DiasSemana;
import com.gustavo.proyecto_persona.enums.TipoMateria;
import com.gustavo.proyecto_persona.model.Horario;
import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.model.Perfil;
import com.gustavo.proyecto_persona.model.Usuario;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

public class DataProvider {

    public static List<Perfil> getPerfilesMockParaFindByUsuarioUsername() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("Gus123");
        usuario.setPassword("123456789");
        usuario.setRol("ADMIN");
        usuario.setMaterias(null);
        usuario.setPerfiles(null);

        return List.of(
                new Perfil(1L, "Gustavo", usuario, new HashSet<>()),
                new Perfil(2L, "Maria", usuario, new HashSet<>()),
                new Perfil(3L, "Pedro", usuario, new HashSet<>())
        );
    }

    public static Perfil getPerfilMock() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("Gus123");
        usuario.setPassword("123456789");
        usuario.setRol("ADMIN");
        usuario.setMaterias(null);
        usuario.setPerfiles(null);

        return new Perfil(99999L, "gustavo f", usuario, null);
    }

    public static Usuario getUsuarioMock() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("Gus123");
        usuario.setPassword("123456789");
        usuario.setRol("ADMIN");
        usuario.setMaterias(null);
        usuario.setPerfiles(null);

        return usuario;
    }

    public static List<Horario> getHorariosMock() {

        Materia quimica = new Materia();
        Materia matematicaDiscreta = new Materia();

        quimica.setId(1L);
        quimica.setNombre("Química I");
        quimica.setTipoMateria(TipoMateria.CUATRIMESTRAL);
        quimica.setAnioCursada(2026);
        quimica.setHorarios(null);
        quimica.setUsuario(getUsuarioMock());
        quimica.setInscripciones(null);

        matematicaDiscreta.setId(2L);
        matematicaDiscreta.setNombre("Matemática Discreta I");
        matematicaDiscreta.setTipoMateria(TipoMateria.CUATRIMESTRAL);
        matematicaDiscreta.setAnioCursada(2026);
        matematicaDiscreta.setHorarios(null);
        matematicaDiscreta.setUsuario(getUsuarioMock());
        matematicaDiscreta.setInscripciones(null);

        return List.of(
                new Horario(1L, LocalTime.of(10, 0),
                        LocalTime.of(12, 0), DiasSemana.LUNES,
                        TipoMateria.TEORIA, quimica),

                new Horario(2L, LocalTime.of(12, 0),
                        LocalTime.of(14, 0), DiasSemana.MARTES,
                        TipoMateria.PRACTICA, quimica),

                new Horario(3L, LocalTime.of(10, 0),
                        LocalTime.of(12, 0), DiasSemana.MIERCOLES,
                        TipoMateria.TEORIA, matematicaDiscreta),

                new Horario(4L, LocalTime.of(10, 0),
                        LocalTime.of(12, 0), DiasSemana.JUEVES,
                        TipoMateria.PRACTICA, matematicaDiscreta)
        );
    }

    public static Horario getHorarioMock() {

        Materia quimica = new Materia();

        quimica.setId(1L);
        quimica.setNombre("Química I");
        quimica.setTipoMateria(TipoMateria.CUATRIMESTRAL);
        quimica.setAnioCursada(2026);
        quimica.setHorarios(null);
        quimica.setUsuario(getUsuarioMock());
        quimica.setInscripciones(null);

        return new Horario(1L, LocalTime.of(10, 0),
                LocalTime.of(12, 0), DiasSemana.LUNES,
                TipoMateria.TEORIA, quimica);
    }

    public static Horario getHorarioSinIdMock() {

        Materia quimica = new Materia();

        quimica.setId(1L);
        quimica.setNombre("Química I");
        quimica.setTipoMateria(TipoMateria.CUATRIMESTRAL);
        quimica.setAnioCursada(2026);
        quimica.setHorarios(null);
        quimica.setUsuario(getUsuarioMock());
        quimica.setInscripciones(null);

        return new Horario(null, LocalTime.of(10, 0),
                LocalTime.of(12, 0), DiasSemana.LUNES,
                TipoMateria.TEORIA, quimica);
    }
}
