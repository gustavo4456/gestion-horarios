package com.gustavo.proyecto_persona.dto.rest;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionSimpleDto {

    private Long id;
    @NotNull(message = "Anio lectivo no debe ser nulo.")
    private Integer anioLectivo;

    @NotNull(message = "El id del perfil no debe ser nulo.")
    private Long idPerfil;

}
