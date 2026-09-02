package com.gustavo.proyecto_persona.dto.rest;

import com.gustavo.proyecto_persona.enums.TipoMateria;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaDto {

    private Long id;
    private String nombre;
    private Integer anioCursada;
    private TipoMateria tipoMateria;

    @Builder.Default
    private Set<HorarioDto> horariosDto = new HashSet<>();


}
