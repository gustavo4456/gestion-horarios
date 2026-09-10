package com.gustavo.proyecto_persona.dto.rest;

import com.gustavo.proyecto_persona.model.Inscripcion;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class InscripcionMapper {

    public static InscripcionDto toDto(Inscripcion inscripcion) {

        Set<MateriaDto> materiasDto = inscripcion.getMaterias() == null ? new HashSet<>() : inscripcion.getMaterias()
                .stream()
                .map(materia -> MateriaDto.builder()
                        .id(materia.getId())
                        .nombre(materia.getNombre())
                        .anioCursada(materia.getAnioCursada())
                        .tipoMateria(materia.getTipoMateria())
                        .horariosDto(materia.getHorarios() == null ? new HashSet<>() : materia.getHorarios().stream()
                                .map(horario -> HorarioDto.builder()
                                        .id(horario.getId())
                                        .horaInicio(horario.getHoraInicio())
                                        .horaFin(horario.getHoraFin())
                                        .tipoMateria(horario.getTipoMateria())
                                        .diasSemana(horario.getDiasSemana())
                                        .build())
                                .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toSet());

        return InscripcionDto.builder()
                .id(inscripcion.getId())
                .anioLectivo(inscripcion.getAnioLectivo())
                .materiasDto(materiasDto)
                .build();
    }
}
