package com.gustavo.proyecto_persona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.proyecto_persona.model.Horario;
import com.gustavo.proyecto_persona.repository.HorarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class HorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioService(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    public List<Horario> getHorarios() {
        return horarioRepository.findAllByOrderByMateriaNombreAscHoraInicioAsc();
    }

    public List<Horario> getHorariosPorAnioCursada(Long id) throws EntityNotFoundException {
        if (id <= 0 || id == null) {
            throw new EntityNotFoundException("El año de cursada debe ser mayor a cero y no ser nulo.");
        }

        return horarioRepository.findHorariosPorAnioCursada(id);
    }

    public Horario getHorarioPorId(Long id) throws EntityNotFoundException {
        Optional<Horario> horarioOpt = horarioRepository.findById(id);

        if (horarioOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro un horario asociado a este id.");
        }

        return horarioOpt.get();
    }

    public void borrarHorario(Long id) throws EntityNotFoundException {
        boolean existeHorario = horarioRepository.existsById(id);

        if (!existeHorario) {
            throw new EntityNotFoundException("No se pudo borrar el horario, el id no esta registrado.");
        }

        horarioRepository.deleteById(id);
    }

    public void guardarHorario(Horario horarioNuevo) throws EntityNotFoundException {
        if (horarioNuevo.getId() != null) {
            throw new EntityNotFoundException("El id del horario debe ser null.");
        }

        horarioRepository.save(horarioNuevo);
    }

    public void actualizarHorario(Horario horario) throws EntityNotFoundException {
        if (horario.getId() == null) {
            throw new EntityNotFoundException("Para actualizar un horario, su id no puede ser null.");
        }

        Optional<Horario> horarioOpt = horarioRepository.findById(horario.getId());

        if (horarioOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro el horario a actualizar.");
        }

        Horario horarioEncontrado = horarioOpt.get();

        horarioEncontrado.setHoraInicio(horario.getHoraInicio());
        horarioEncontrado.setHoraFin(horario.getHoraFin());
        horarioEncontrado.setDiasSemana(horario.getDiasSemana());
        horarioEncontrado.setTipoMateria(horario.getTipoMateria());
        horarioEncontrado.setMateria(horario.getMateria());

        horarioRepository.save(horarioEncontrado);
    }

}
