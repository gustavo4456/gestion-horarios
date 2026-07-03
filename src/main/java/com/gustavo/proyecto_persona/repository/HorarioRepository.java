package com.gustavo.proyecto_persona.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gustavo.proyecto_persona.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findAllByOrderByMateriaNombreAscHoraInicioAsc();

    @Query("SELECT h FROM Horario h JOIN h.materia m WHERE m.anioCursada = :anioCursada ORDER BY m.nombre ASC, h.horaInicio ASC")
    List<Horario> findHorariosPorAnioCursada(@Param("anioCursada") Long anioCursada);
}
