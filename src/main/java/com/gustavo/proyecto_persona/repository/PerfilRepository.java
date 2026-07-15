package com.gustavo.proyecto_persona.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.proyecto_persona.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    // 🌟 Busca únicamente los perfiles asociados al username del dueño
    List<Perfil> findByUsuarioUsername(String username);

    // 🌟 Busca por ID pero asegura que pertenezca al usuario conectado
    Optional<Perfil> findByIdAndUsuarioUsername(Long id, String username);

    // 🌟 Verifica la existencia combinando ID y dueño para un borrado seguro
    boolean existsByIdAndUsuarioUsername(Long id, String username);
}
