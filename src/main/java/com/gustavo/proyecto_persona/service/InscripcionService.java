package com.gustavo.proyecto_persona.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.proyecto_persona.model.Inscripcion;
import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.repository.InscripcionRepository;
import com.gustavo.proyecto_persona.repository.PerfilRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final MateriaService materiaService;
    private final PerfilRepository perfilRepository;

    public InscripcionService(InscripcionRepository inscripcionRepository, MateriaService materiaService,
            PerfilRepository perfilRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.materiaService = materiaService;
        this.perfilRepository = perfilRepository;
    }

    public List<Inscripcion> getInscripciones(String username) {
        return inscripcionRepository.findAllByPerfilUsuarioUsername(username);
    }

    public Inscripcion getInscripcionPorId(Long id, String username) throws EntityNotFoundException {
        return inscripcionRepository.findByIdAndPerfilUsuarioUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la inscripción o no tienes permisos para verla."));
    }

    public void borrarInscripcion(Long id, String username) throws EntityNotFoundException {
        boolean existeInscripcion = inscripcionRepository.existsByIdAndPerfilUsuarioUsername(id, username);

        if (!existeInscripcion) {
            throw new EntityNotFoundException("No se encontró la inscripción a borrar o no tienes permisos.");
        }

        inscripcionRepository.deleteById(id);
    }

    public void guardarInscripcion(Inscripcion inscripcion, List<Long> seleccion, String username)
            throws EntityNotFoundException {

        if (inscripcion.getId() != null) {
            throw new EntityNotFoundException("Para guardar una inscripción el id debe ser null.");
        }

        if (seleccion == null || seleccion.isEmpty()) {
            throw new EntityNotFoundException("No seleccionó materias.");
        }

        // 🌟 VALIDACIÓN 1: ¿El perfil (persona) elegido le pertenece al usuario?
        boolean perfilPertenece = perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(),
                username);
        if (!perfilPertenece) {
            throw new EntityNotFoundException("La persona seleccionada no pertenece a tu usuario.");
        }

        // 🌟 VALIDACIÓN 2: ¿Ya existe inscripción para este año en su cuenta?
        boolean existe = inscripcionRepository.existsByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(
                inscripcion.getPerfil().getId(),
                inscripcion.getAnioLectivo(),
                username);

        if (existe) {
            throw new EntityNotFoundException("La persona ya tiene una inscripción para ese año lectivo.");
        }

        Inscripcion inscripcionGuardada = inscripcionRepository.save(inscripcion);

        // 🌟 VALIDACIÓN 3: Al buscar materias con `getMateriaPorId(..., username)`,
        // si alguna materia elegida no le pertenece al usuario, tirará una excepción
        // automáticamente.
        for (Long idMateria : seleccion) {
            Materia mat = materiaService.getMateriaPorId(idMateria, username);
            inscripcionGuardada.agregarMateria(mat);
        }

        inscripcionRepository.save(inscripcionGuardada);
    }

    public void actualizarInscripcion(Inscripcion inscripcion, List<Long> nuevasMateriasIds, String username)
            throws EntityNotFoundException {

        if (inscripcion.getId() == null) {
            throw new EntityNotFoundException("Para actualizar datos de una inscripción el id no debe ser nulo.");
        }

        // 🌟 VALIDACIÓN 1: ¿La inscripción original es de este usuario?
        Inscripcion inscripcionDB = inscripcionRepository
                .findByIdAndPerfilUsuarioUsername(inscripcion.getId(), username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la inscripción a editar o no tienes permisos."));

        // 🌟 VALIDACIÓN 2: ¿El perfil nuevo al que se cambia pertenece al usuario?
        boolean perfilPertenece = perfilRepository.existsByIdAndUsuarioUsername(inscripcion.getPerfil().getId(),
                username);
        if (!perfilPertenece) {
            throw new EntityNotFoundException("La persona seleccionada no pertenece a tu usuario.");
        }

        inscripcionDB.setAnioLectivo(inscripcion.getAnioLectivo());
        inscripcionDB.setPerfil(inscripcion.getPerfil());

        // Desvincular las materias viejas
        for (Materia matDB : inscripcionDB.getMaterias()) {
            matDB.getInscripciones().remove(inscripcionDB);
        }
        inscripcionDB.getMaterias().clear();

        // 🌟 VALIDACIÓN 3: Vincular las nuevas materias validando pertenencia
        if (nuevasMateriasIds != null && !nuevasMateriasIds.isEmpty()) {
            for (Long idMateria : nuevasMateriasIds) {
                Materia nuevaMat = materiaService.getMateriaPorId(idMateria, username);
                inscripcionDB.agregarMateria(nuevaMat);
            }
        }

        inscripcionRepository.save(inscripcionDB);
    }

    public List<Inscripcion> getInscripcionesPorPersona(Long idPersona, String username)
            throws EntityNotFoundException {
        if (idPersona <= 0) {
            throw new EntityNotFoundException("Debe seleccionar una persona.");
        }
        return inscripcionRepository.findByPerfilIdAndPerfilUsuarioUsername(idPersona, username);
    }

    public List<Inscripcion> getInscripcionesPorAnioLectivo(Integer anioLectivo, String username)
            throws EntityNotFoundException {
        if (anioLectivo <= 0) {
            throw new EntityNotFoundException("Debe ingresar un año lectivo positivo.");
        }
        return inscripcionRepository.findByAnioLectivoAndPerfilUsuarioUsername(anioLectivo, username);
    }

    public List<Inscripcion> getInscripcionesPorPersonaYAnioLectivo(Long idPersona, Integer anioLectivo,
            String username)
            throws EntityNotFoundException {
        if (anioLectivo <= 0 || idPersona <= 0) {
            throw new EntityNotFoundException("Debe ingresar un año lectivo positivo y seleccionar una persona.");
        }
        return inscripcionRepository.findByPerfilIdAndAnioLectivoAndPerfilUsuarioUsername(idPersona, anioLectivo,
                username);
    }

}
