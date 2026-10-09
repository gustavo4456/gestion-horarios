package com.gustavo.proyecto_persona;

import com.gustavo.proyecto_persona.model.Perfil;
import com.gustavo.proyecto_persona.model.Usuario;

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
}
