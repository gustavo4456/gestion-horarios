package com.gustavo.proyecto_persona.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.proyecto_persona.model.Materia;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    // 🔍 1. Filtrar por nombre (busca coincidencia exacta)
    List<Materia> findByNombre(String nombre);

    // 🔍 2. Filtrar por nombre usando "LIKE" (Trae si contiene una parte del texto,
    // ideal para buscadores)
    List<Materia> findByNombreContainingIgnoreCase(String nombre);

    // 🔍 3. Filtrar por año de cursada
    List<Materia> findByAnioCursada(Integer anioCursada);

    // 🔍 4. Combinado: Filtrar por nombre AND año de cursada (por si lo necesitas
    // juntos)
    List<Materia> findByNombreContainingIgnoreCaseAndAnioCursada(String nombre, Integer anioCursada);
}
