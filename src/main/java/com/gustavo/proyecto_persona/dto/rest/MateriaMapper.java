package com.gustavo.proyecto_persona.dto.rest;

import com.gustavo.proyecto_persona.model.Materia;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MateriaMapper {

    public static MateriaDto toDto(Materia materia) {

        Set<HorarioDto> horarioDto = materia.getHorarios() == null ? new HashSet<>() : materia.getHorarios().stream()
                .map(horario -> HorarioDto.builder()
                        .id(horario.getId())
                        .horaInicio(horario.getHoraInicio())
                        .horaFin(horario.getHoraFin())
                        .diasSemana(horario.getDiasSemana())
                        .tipoMateria(horario.getTipoMateria())
                        .build())
                .collect(Collectors.toSet());

        return MateriaDto.builder()
                .id(materia.getId())
                .nombre(materia.getNombre())
                .anioCursada(materia.getAnioCursada())
                .tipoMateria(materia.getTipoMateria())
                .horariosDto(horarioDto)
                .build();

    }
}
