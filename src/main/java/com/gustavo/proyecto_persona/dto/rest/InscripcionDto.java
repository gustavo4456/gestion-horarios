package com.gustavo.proyecto_persona.dto.rest;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionDto {

    private Long id;
    private Integer anioLectivo;

    @Builder.Default
    private Set<MateriaDto> materiasDto = new HashSet<>();

}
