package com.gustavo.proyecto_persona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.model.Usuario;
import com.gustavo.proyecto_persona.repository.MateriaRepository;
import com.gustavo.proyecto_persona.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final UsuarioRepository usuarioRepository;

    public MateriaService(MateriaRepository materiaRepository, UsuarioRepository usuarioRepository) {
        this.materiaRepository = materiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Materia> getMaterias(String username) {
        return materiaRepository.findByUsuarioUsername(username);
    }

    public Materia getMateriaPorId(Long id, String username) throws EntityNotFoundException {
        return materiaRepository.findByIdAndUsuarioUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la materia o no tienes permisos para verla."));
    }

    public void borrarMateria(Long id, String username) throws EntityNotFoundException {
        boolean existeMateria = materiaRepository.existsByIdAndUsuarioUsername(id, username);

        if (!existeMateria) {
            throw new EntityNotFoundException("No se pudo eliminar la materia: no existe o no tienes permisos.");
        }

        materiaRepository.deleteById(id);
    }

    public void guardarMateria(Materia materia, String username) throws EntityNotFoundException {
        if (materia.getId() != null) {
            throw new EntityNotFoundException("Para poder crear una materia el id debe ser null.");
        }

        // Recuperamos el usuario de la BD usando el username de la sesión
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException("No se encontró el usuario logueado en la base de datos."));

        materia.setUsuario(usuario);

        materiaRepository.save(materia);
    }

    public void actualizarMateria(Materia materia, String username) throws EntityNotFoundException {
        if (materia.getId() == null) {
            throw new EntityNotFoundException("El id de la materia no puede ser null para actualizar sus datos.");
        }

        // Verificamos que la materia original pertenezca al usuario antes de
        // modificarla
        Materia materiaBD = materiaRepository.findByIdAndUsuarioUsername(materia.getId(), username)
                .orElseThrow(
                        () -> new EntityNotFoundException("No se encontró la materia a editar o no te pertenece."));

        materiaBD.setNombre(materia.getNombre());
        materiaBD.setTipoMateria(materia.getTipoMateria());
        materiaBD.setAnioCursada(materia.getAnioCursada());

        materiaRepository.save(materiaBD);
    }

    public List<Materia> buscarPorNombre(String nombre, String username) {
        return materiaRepository.findByNombreContainingIgnoreCaseAndUsuarioUsername(nombre, username);
    }

    public List<Materia> buscarPorAnioCursada(Integer anioCursada, String username) throws EntityNotFoundException {
        if (anioCursada <= 0) {
            throw new EntityNotFoundException("El año lectivo debe ser mayor a 0.");
        }
        return materiaRepository.findByAnioCursadaAndUsuarioUsername(anioCursada, username);
    }

    public List<Materia> buscarPorNombreYAanioCursada(String nombre, Integer anioCursada, String username) {
        if (anioCursada <= 0) {
            throw new EntityNotFoundException("El año lectivo debe ser mayor a 0.");
        }
        return materiaRepository.findByNombreContainingIgnoreCaseAndAnioCursadaAndUsuarioUsername(nombre, anioCursada,
                username);
    }

}
