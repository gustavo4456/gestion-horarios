package com.gustavo.proyecto_persona.dto.rest;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilDto {

    private Long id;
    private String nombrePerfil;
    private String nombreUsuario;

    @Builder.Default
    private Set<InscripcionDto> inscripcionesDto = new HashSet<>();
}
