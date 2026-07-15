package com.gustavo.proyecto_persona.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.proyecto_persona.model.Materia;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    List<Materia> findByUsuarioUsername(String username);

    Optional<Materia> findByIdAndUsuarioUsername(Long id, String username);

    boolean existsByIdAndUsuarioUsername(Long id, String username);

    List<Materia> findByNombreContainingIgnoreCaseAndUsuarioUsername(String nombre, String username);

    List<Materia> findByAnioCursadaAndUsuarioUsername(Integer anioCursada, String username);

    List<Materia> findByNombreContainingIgnoreCaseAndAnioCursadaAndUsuarioUsername(String nombre, Integer anioCursada,
            String username);
}
