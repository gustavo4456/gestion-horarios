package com.gustavo.proyecto_persona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.proyecto_persona.model.Horario;
import com.gustavo.proyecto_persona.repository.HorarioRepository;
import com.gustavo.proyecto_persona.repository.MateriaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final MateriaRepository materiaRepository;

    public HorarioService(HorarioRepository horarioRepository, MateriaRepository materiaRepository) {
        this.horarioRepository = horarioRepository;
        this.materiaRepository = materiaRepository;
    }

    public List<Horario> getHorarios(String username) {
        return horarioRepository.findAllByMateriaUsuarioUsernameOrderByMateriaNombreAscHoraInicioAsc(username);
    }

    public List<Horario> getHorariosPorAnioCursada(Long id, String username) throws EntityNotFoundException {
        if (id == null || id <= 0) {
            throw new EntityNotFoundException("El año de cursada debe ser mayor a cero y no ser nulo.");
        }

        return horarioRepository.findHorariosPorAnioCursadaYUsuario(id, username);
    }

    public Horario getHorarioPorId(Long id, String username) throws EntityNotFoundException {
        return horarioRepository.findByIdAndMateriaUsuarioUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el horario o no tienes permisos para acceder."));
    }

    public void borrarHorario(Long id, String username) throws EntityNotFoundException {
        boolean existeHorario = horarioRepository.existsByIdAndMateriaUsuarioUsername(id, username);

        if (!existeHorario) {
            throw new EntityNotFoundException("No se pudo borrar el horario: no existe o no tienes permisos.");
        }

        horarioRepository.deleteById(id);
    }

    public void guardarHorario(Horario horarioNuevo, String username) throws EntityNotFoundException {
        if (horarioNuevo.getId() != null) {
            throw new EntityNotFoundException("El id del horario debe ser null.");
        }

        boolean materiaPerteneceAlUsuario = materiaRepository.existsByIdAndUsuarioUsername(
                horarioNuevo.getMateria().getId(), username);

        if (!materiaPerteneceAlUsuario) {
            throw new EntityNotFoundException("No puedes asignar horarios a una materia que no te pertenece.");
        }

        horarioRepository.save(horarioNuevo);
    }

    public void actualizarHorario(Horario horario, String username) throws EntityNotFoundException {
        if (horario.getId() == null) {
            throw new EntityNotFoundException("Para actualizar un horario, su id no puede ser null.");
        }

        // 🌟 Verificamos que el horario que quiere modificar realmente le pertenezca
        Horario horarioEncontrado = horarioRepository.findByIdAndMateriaUsuarioUsername(horario.getId(), username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró el horario a actualizar o no tienes permisos."));

        // 🌟 Verificamos que la materia seleccionada para actualizar también le
        // pertenezca
        boolean materiaPerteneceAlUsuario = materiaRepository.existsByIdAndUsuarioUsername(
                horario.getMateria().getId(), username);

        if (!materiaPerteneceAlUsuario) {
            throw new EntityNotFoundException("No puedes transferir este horario a una materia que no te pertenece.");
        }

        horarioEncontrado.setHoraInicio(horario.getHoraInicio());
        horarioEncontrado.setHoraFin(horario.getHoraFin());
        horarioEncontrado.setDiasSemana(horario.getDiasSemana());
        horarioEncontrado.setTipoMateria(horario.getTipoMateria());
        horarioEncontrado.setMateria(horario.getMateria());

        horarioRepository.save(horarioEncontrado);
    }

}
