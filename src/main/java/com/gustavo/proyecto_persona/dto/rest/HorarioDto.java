package com.gustavo.proyecto_persona.dto.rest;

import com.gustavo.proyecto_persona.enums.DiasSemana;
import com.gustavo.proyecto_persona.enums.TipoMateria;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioDto {

    private Long id;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private DiasSemana diasSemana;
    private TipoMateria tipoMateria;
}
