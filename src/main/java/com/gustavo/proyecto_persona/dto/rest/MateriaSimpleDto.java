package com.gustavo.proyecto_persona.dto.rest;

import com.gustavo.proyecto_persona.enums.TipoMateria;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaSimpleDto {

    private Long id;
    private String nombre;
    private Integer anioCursada;
    private TipoMateria tipoMateria;
}
