package com.gustavo.proyecto_persona.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@ToString(exclude = { "id", "materias", "persona" })
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = { "persona_id", "anio_lectivo" })
})
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "EL anio lectivo es obligatorio.")
    private Integer anioLectivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id")
    @NotNull(message = "Debe seleccionar una persona.")
    private Persona persona;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "inscripcion_materia", joinColumns = @JoinColumn(name = "inscripcion_id"), inverseJoinColumns = @JoinColumn(name = "materia_id"))
    private Set<Materia> materias = new HashSet<>();

    public void agregarMateria(Materia materia) {
        this.materias.add(materia);
        materia.getInscripciones().add(this); // Avisa a la materia que tiene esta inscripción
    }

}
