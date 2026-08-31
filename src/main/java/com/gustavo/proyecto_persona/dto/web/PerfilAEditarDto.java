package com.gustavo.proyecto_persona.dto.web;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "id")
public class PerfilAEditarDto {

    private Long id;

    @NotBlank(message = "El nombre de la persona es obligatorio.")
    private String nombre;
}
