package com.gustavo.proyecto_persona.model;

import java.util.HashSet;
import java.util.Set;

import com.gustavo.proyecto_persona.enums.TipoMateria;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "horarios", "inscripciones" })
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 170)
    @NotBlank(message = "La materia necesita un nombre.")
    private String nombre;

    @NotNull(message = "El año de la materia es obligatorio.")
    private Integer anioCursada;

    @NotNull(message = "Tipo de materia es obligatorio.")
    @Enumerated(EnumType.STRING)
    private TipoMateria tipoMateria;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Horario> horarios = new HashSet<>();

    @ManyToMany(mappedBy = "materias")
    private Set<Inscripcion> inscripciones = new HashSet<>();

    public void agregarHorario(Horario horario) {
        this.horarios.add(horario);
        horario.setMateria(this);
    }
}
