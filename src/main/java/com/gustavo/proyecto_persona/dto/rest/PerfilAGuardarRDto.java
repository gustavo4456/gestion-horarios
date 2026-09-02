package com.gustavo.proyecto_persona.dto.rest;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PerfilAGuardarRDto {
    @NotBlank(message = "El nombre de la persona es obligatorio.")
    private String nombre;
}
