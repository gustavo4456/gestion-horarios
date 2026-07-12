package com.gustavo.proyecto_persona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.proyecto_persona.model.Inscripcion;
import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.repository.InscripcionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final MateriaService materiaService;

    public InscripcionService(InscripcionRepository inscripcionRepository, MateriaService materiaService) {
        this.inscripcionRepository = inscripcionRepository;
        this.materiaService = materiaService;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripcionRepository.findAll();
    }

    public Inscripcion getInscripcionPorId(Long id) throws EntityNotFoundException {

        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(id);

        if (inscripcionOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontro la inscripcion asociada a este id.");
        }

        return inscripcionOpt.get();
    }

    public void borrarInscripcion(Long id) throws EntityNotFoundException {

        boolean existeInscripcion = inscripcionRepository.existsById(id);

        if (!existeInscripcion) {
            throw new EntityNotFoundException("NO se encontro la inscripcion a borrar.");
        }

        inscripcionRepository.deleteById(id);
    }

    public void guardarInscripcion(Inscripcion inscripcion, List<Long> seleccion) throws EntityNotFoundException {

        if (inscripcion.getId() != null) {
            throw new EntityNotFoundException("Para guardar una inscripcion el id debe ser null.");
        }

        if (seleccion == null || seleccion.isEmpty()) {

            throw new EntityNotFoundException("No selecciono materias.");

        }

        boolean existe = inscripcionRepository
                .existsByPerfilIdAndAnioLectivo(
                        inscripcion.getPerfil().getId(),
                        inscripcion.getAnioLectivo());

        if (existe) {
            throw new EntityNotFoundException(
                    "La persona ya tiene una inscripción para ese año lectivo.");
        }

        Inscripcion inscripcionGuardada = inscripcionRepository.save(inscripcion);

        for (Long idMateria : seleccion) {
            Materia mat = materiaService.getMateriaPorId(idMateria);

            inscripcionGuardada.agregarMateria(mat);
        }

        inscripcionRepository.save(inscripcionGuardada);
    }

    public void actualizarInscripcion(Inscripcion inscripcion, List<Long> nuevasMateriasIds)
            throws EntityNotFoundException {

        // 1. Validamos que venga el ID
        if (inscripcion.getId() == null) {
            throw new EntityNotFoundException("Para actualizar datos de una inscripcion el id no debe ser nulo.");
        }

        // 2. Buscamos la inscripción original que está guardada en la BD
        Inscripcion inscripcionDB = inscripcionRepository.findById(inscripcion.getId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la inscripción a editar."));

        // 3. Actualizamos los datos básicos del formulario
        inscripcionDB.setAnioLectivo(inscripcion.getAnioLectivo());
        inscripcionDB.setPerfil(inscripcion.getPerfil());

        // 🚀 PASO CLAVE: Desvincular las materias viejas de forma bidireccional
        for (Materia matDB : inscripcionDB.getMaterias()) {
            matDB.getInscripciones().remove(inscripcionDB); // Le avisamos a la materia que ya no tiene a este alumno
        }
        inscripcionDB.getMaterias().clear(); // Vaciamos por completo la lista vieja de la inscripción

        // 🚀 PASO CLAVE 2: Llenar con la nueva lista que mandó el usuario
        if (nuevasMateriasIds != null && !nuevasMateriasIds.isEmpty()) {
            for (Long idMateria : nuevasMateriasIds) {
                Materia nuevaMat = materiaService.getMateriaPorId(idMateria);
                inscripcionDB.agregarMateria(nuevaMat); // Agregamos la nueva materia usando tu helper
            }
        }

        // 4. Guardamos los cambios. Hibernate se encarga de borrar los registros viejos
        // de la tabla intermedia 'inscripcion_materia' e insertar los nuevos
        // automáticamente.
        inscripcionRepository.save(inscripcionDB);

    }

    public List<Inscripcion> getInscripcionesPorPersona(Long idPersona) throws EntityNotFoundException {

        if (idPersona <= 0) {
            throw new EntityNotFoundException("Debe seleccionar una persona.");
        }

        return inscripcionRepository.findByPerfilId(idPersona);
    }

    public List<Inscripcion> getInscripcionesPorAnioLectivo(Integer anioLectivo) throws EntityNotFoundException {

        if (anioLectivo <= 0) {
            throw new EntityNotFoundException("Debe ingresar un año lectivo positivo.");
        }

        return inscripcionRepository.findByAnioLectivo(anioLectivo);
    }

    public List<Inscripcion> getInscripcionesPorPersonaYAnioLectivo(Long idPersona, Integer anioLectivo)
            throws EntityNotFoundException {

        if (anioLectivo <= 0 || idPersona <= 0) {
            throw new EntityNotFoundException("Debe ingresar un año lectivo positivo y seleccionar una persona.");
        }

        return inscripcionRepository.findByPerfilIdAndAnioLectivo(idPersona, anioLectivo);
    }

}
