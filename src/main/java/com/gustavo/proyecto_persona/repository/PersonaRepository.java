package com.gustavo.proyecto_persona.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.proyecto_persona.model.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

}
