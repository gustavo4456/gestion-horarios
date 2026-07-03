package com.gustavo.proyecto_persona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.repository.MateriaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class MateriaService {

    private final MateriaRepository materiaRepository;

    public MateriaService(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;

    }

    public List<Materia> getMaterias() {
        return materiaRepository.findAll();
    }

    public Materia getMateriaPorId(Long id) throws EntityNotFoundException {

        Optional<Materia> materiaOp = materiaRepository.findById(id);

        if (materiaOp.isEmpty()) {
            throw new EntityNotFoundException("El id de esta materia no existe en la bd.");
        }

        return materiaOp.get();
    }

    public void borrarMateria(Long id) throws EntityNotFoundException {

        boolean existeMateria = materiaRepository.existsById(id);

        if (!existeMateria) {
            throw new EntityNotFoundException("No se pudo eliminar la materia, el id no existe en la bd.");
        }

        materiaRepository.deleteById(id);
    }

    public void guardarMateria(Materia materia) throws EntityNotFoundException {

        if (materia.getId() != null) {
            throw new EntityNotFoundException("Para poder crear una materia el id debe ser null.");
        }

        materiaRepository.save(materia);

    }

    public void actualizarMateria(Materia materia) throws EntityNotFoundException {

        if (materia.getId() == null) {
            throw new EntityNotFoundException("EL id de la materia no puede ser null para actualizar sus datos.");
        }

        Optional<Materia> materiaOpt = materiaRepository.findById(materia.getId());

        if (materiaOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro la materia a editar.");
        }

        Materia materiaBD = materiaOpt.get();

        materiaBD.setNombre(materia.getNombre());
        materiaBD.setTipoMateria(materia.getTipoMateria());
        materiaBD.setAnioCursada(materia.getAnioCursada());

        materiaRepository.save(materiaBD);
    }

    public List<Materia> buscarPorNombre(String nombre) {

        return materiaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Materia> buscarPorAnioCursada(Integer anioCursada) throws EntityNotFoundException {

        if (anioCursada <= 0) {
            throw new EntityNotFoundException("El año lectivo debe ser mayor a 0.");
        }

        return materiaRepository.findByAnioCursada(anioCursada);
    }

    public List<Materia> buscarPorNombreYAanioCursada(String nombre, Integer anioCursada) {

        if (anioCursada <= 0) {
            throw new EntityNotFoundException("El año lectivo debe ser mayor a 0.");
        }

        return materiaRepository.findByNombreContainingIgnoreCaseAndAnioCursada(nombre, anioCursada);
    }

}
