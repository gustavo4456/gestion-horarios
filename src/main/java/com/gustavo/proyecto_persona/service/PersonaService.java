package com.gustavo.proyecto_persona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.proyecto_persona.dto.PersonaAEditarDto;
import com.gustavo.proyecto_persona.dto.PersonaAGuardarDto;
import com.gustavo.proyecto_persona.model.Persona;
import com.gustavo.proyecto_persona.repository.PersonaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<Persona> getPersonas() {
        return personaRepository.findAll();
    }

    public void guardarPersona(PersonaAGuardarDto persona) {

        Persona personaAGuardar = new Persona();
        personaAGuardar.setNombre(persona.getNombre());
        personaAGuardar.setApellido(persona.getApellido());
        personaAGuardar.setEdad(persona.getEdad());
        personaAGuardar.setDni(persona.getDni());

        personaRepository.save(personaAGuardar);
    }

    public void actualizarPersona(PersonaAEditarDto persona) throws EntityNotFoundException {

        Optional<Persona> personaEncontradaOp = personaRepository.findById(persona.getId());

        if (personaEncontradaOp.isEmpty()) {
            throw new EntityNotFoundException("No se pudo actualizar los datos, no se encontro a la persona. ");
        }

        Persona personaEncontrada = personaEncontradaOp.get();

        personaEncontrada.setNombre(persona.getNombre());
        personaEncontrada.setApellido(persona.getApellido());
        personaEncontrada.setEdad(persona.getEdad());
        personaEncontrada.setDni(persona.getDni());

        personaRepository.save(personaEncontrada);
    }

    public Persona getPersonaPorId(Long id) throws EntityNotFoundException {

        Optional<Persona> persona = personaRepository.findById(id);

        if (persona.isEmpty()) {
            throw new EntityNotFoundException("Persona no encontrada en la bd.");
        }

        return persona.get();
    }

    public void borrarPersona(Long id) throws EntityNotFoundException {

        boolean existePersona = personaRepository.existsById(id);

        if (!existePersona) {
            throw new EntityNotFoundException("No se encontro el id , no se pudo borrar la persona.");
        }

        personaRepository.deleteById(id);
    }

}
