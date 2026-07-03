package com.gustavo.proyecto_persona.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "dni")
@ToString(exclude = { "id", "inscripciones" })
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    @NotBlank(message = "El nombre de persona es obligatorio.")
    private String nombre;

    @Column(nullable = false, length = 120)
    @NotBlank(message = "El apellido de la persona es obligatorio.")
    private String apellido;

    @Column(nullable = false)
    @NotNull
    @Min(value = 18, message = "No se puede guardar personas menores de 18 años.")
    private Integer edad;

    @Column(nullable = false, length = 120, unique = true)
    @NotBlank(message = "El dni de la persona es obligatorio.")
    private String dni;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Inscripcion> inscripciones = new HashSet<>();

    public void agregarInscripcion(Inscripcion inscripcion) {
        this.inscripciones.add(inscripcion);
        inscripcion.setPersona(this);
    }
}
