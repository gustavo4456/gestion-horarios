package com.gustavo.proyecto_persona.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gustavo.proyecto_persona.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    // 🔒 Listar ordenado filtrando solo por los horarios del usuario logueado
    List<Horario> findAllByMateriaUsuarioUsernameOrderByMateriaNombreAscHoraInicioAsc(String username);

    // 🔒 Buscar horario por ID asegurando que la materia asociada le pertenezca al
    // usuario
    Optional<Horario> findByIdAndMateriaUsuarioUsername(Long id, String username);

    // 🔒 Verificar existencia segura de un horario para el usuario antes de borrar
    boolean existsByIdAndMateriaUsuarioUsername(Long id, String username);

    // 🔒 Filtro por año de cursada restringido al usuario activo
    @Query("SELECT h FROM Horario h JOIN h.materia m WHERE m.anioCursada = :anioCursada AND m.usuario.username = :username ORDER BY m.nombre ASC, h.horaInicio ASC")
    List<Horario> findHorariosPorAnioCursadaYUsuario(@Param("anioCursada") Long anioCursada,
            @Param("username") String username);
}
