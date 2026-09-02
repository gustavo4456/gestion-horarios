package com.gustavo.proyecto_persona.dto.rest;

import com.gustavo.proyecto_persona.model.Perfil;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PerfilMapper {

    public static PerfilDto toDto(Perfil perfil) {

        if (perfil == null) {
            return null;
        }

        Set<InscripcionDto> inscripcionesDto = perfil.getInscripciones() == null ? new HashSet<>() :
                perfil.getInscripciones().stream()
                        .map(inscripcion -> InscripcionDto.builder()
                                .id(inscripcion.getId())
                                .anioLectivo(inscripcion.getAnioLectivo())
                                .materiasDto(inscripcion.getMaterias() == null ? new HashSet<>() :
                                        inscripcion.getMaterias().stream()
                                                .map(materia -> MateriaDto.builder()
                                                        .id(materia.getId())
                                                        .anioCursada(materia.getAnioCursada())
                                                        .nombre(materia.getNombre())
                                                        .tipoMateria(materia.getTipoMateria())
                                                        .horariosDto(materia.getHorarios() == null ? new HashSet<>() :
                                                                materia.getHorarios().stream()
                                                                        .map(horario -> HorarioDto.builder()
                                                                                .id(horario.getId())
                                                                                .horaInicio(horario.getHoraInicio())
                                                                                .horaFin(horario.getHoraFin())
                                                                                .diasSemana(horario.getDiasSemana())
                                                                                .tipoMateria(horario.getTipoMateria())
                                                                                .build())
                                                                        .collect(Collectors.toSet()))
                                                        .build())
                                                .collect(Collectors.toSet()))
                                .build()
                        )
                        .collect(Collectors.toSet());

        return PerfilDto.builder()
                .id(perfil.getId())
                .nombrePerfil(perfil.getNombre())
                .nombreUsuario(perfil.getUsuario().getUsername())
                .inscripcionesDto(inscripcionesDto)
                .build();
    }


}
