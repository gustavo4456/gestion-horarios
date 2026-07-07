package com.gustavo.proyecto_persona.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.proyecto_persona.model.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    // 🔍 1. Buscar inscripciones por el ID de la Persona
    List<Inscripcion> findByPersonaId(Long personaId);

    // 🔍 2. Buscar inscripciones por Año Lectivo
    List<Inscripcion> findByAnioLectivo(Integer anioLectivo);

    // 🔍 3. Combinado: Buscar por el ID de la Persona Y el Año Lectivo
    List<Inscripcion> findByPersonaIdAndAnioLectivo(Long personaId, Integer anioLectivo);
}
