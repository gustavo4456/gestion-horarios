package com.gustavo.proyecto_persona.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "dni")
@ToString(exclude = "id")
public class PersonaAEditarDto {

    private Long id;

    @NotBlank(message = "El nombre de la persona es obligatorio.")
    private String nombre;

    @NotBlank(message = "El apellido de la persona es obligatorio.")
    private String apellido;

    @NotNull
    @Min(value = 18, message = "No se permite personas menores de 18 años.")
    private Integer edad;

    @NotBlank(message = "El de dni de la persona es obligatorio.")
    private String dni;
}
