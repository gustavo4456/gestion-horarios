package com.gustavo.proyecto_persona.dto;

import java.time.LocalTime;

import com.gustavo.proyecto_persona.enums.DiasSemana;
import com.gustavo.proyecto_persona.enums.TipoMateria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HorarioDto {
    private String materia;
    private Integer anio;
    private DiasSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private TipoMateria tipoMateria;

    public HorarioDto(String materia,
            Integer anio,
            DiasSemana dia,
            LocalTime horaInicio,
            LocalTime horaFin,
            TipoMateria tipoMateria) {
        this.materia = materia;
        this.anio = anio;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tipoMateria = tipoMateria;
    }
}
