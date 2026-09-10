package com.gustavo.proyecto_persona.dto.rest;

import com.gustavo.proyecto_persona.model.Inscripcion;
import com.gustavo.proyecto_persona.model.Perfil;

import java.util.HashSet;

public class InscripcionSimpleDtoMapper {

    public static Inscripcion toClass(InscripcionSimpleDto inscripcionSimpleDto) {

        Inscripcion inscripcion = new Inscripcion();

        inscripcion.setId(inscripcionSimpleDto.getId());
        inscripcion.setAnioLectivo(inscripcionSimpleDto.getAnioLectivo());

        Perfil perfil = new Perfil();
        perfil.setId(inscripcionSimpleDto.getIdPerfil());

        inscripcion.setPerfil(perfil);

        inscripcion.setMaterias(new HashSet<>());

        return inscripcion;
    }
}
