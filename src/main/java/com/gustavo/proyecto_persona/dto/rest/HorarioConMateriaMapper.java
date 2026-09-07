package com.gustavo.proyecto_persona.dto.rest;

import com.gustavo.proyecto_persona.model.Horario;
import com.gustavo.proyecto_persona.model.Materia;

public class HorarioConMateriaMapper {

    public static HorarioConMateriaDto toDto(Horario horario) {

        Materia materia = horario.getMateria() == null ? new Materia() :
                horario.getMateria();

        MateriaSimpleDto materiaSimpleDto = MateriaSimpleDto.builder()
                .id(materia.getId())
                .nombre(materia.getNombre())
                .anioCursada(materia.getAnioCursada())
                .tipoMateria(materia.getTipoMateria())
                .build();

        return HorarioConMateriaDto.builder()
                .id(horario.getId())
                .horaInicio(horario.getHoraInicio())
                .horaFin(horario.getHoraFin())
                .diasSemana(horario.getDiasSemana())
                .tipoMateria(horario.getTipoMateria())
                .materia(materiaSimpleDto)
                .build();
    }
}
