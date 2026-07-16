package com.gustavo.proyecto_persona.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.proyecto_persona.model.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    // 🔒 1. Listar todas las inscripciones del usuario logueado
    List<Inscripcion> findAllByPerfilUsuarioUsername(String username);

    // 🔒 2. Buscar por ID asegurando pertenencia del perfil asociado
    Optional<Inscripcion> findByIdAndPerfilUsuarioUsername(Long id, String username);

    // 🔒 3. Verificar existencia segura antes de borrar
    boolean existsByIdAndPerfilUsuarioUsername(Long id, String username);

    // 🔒 4. Buscar inscripciones por Persona (Perfil) de un usuario específico
    List<Inscripcion> findByPerfilIdAndPerfilUsuarioUsername(Long personaId, String username);

    // 🔒 5. Buscar inscripciones por Año Lectivo del usuario
    List<Inscripcion> findByAnioLectivoAndPerfilUsuarioUsername(Integer anioLectivo, String username);

    // 🔒 6. Combinado seguro: Persona, Año Lectivo y Usuario
    List<Inscripcion> findByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(Long personaId, Integer anioLectivo,
            String username);

    // 🔒 7. Validación de duplicado segura por usuario
    boolean existsByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(Long personaId, Integer anioLectivo,
            String username);
}
