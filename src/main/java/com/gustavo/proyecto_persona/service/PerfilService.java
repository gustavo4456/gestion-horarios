package com.gustavo.proyecto_persona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.proyecto_persona.dto.PerfilAEditarDto;
import com.gustavo.proyecto_persona.dto.PerfilAGuardarDto;
import com.gustavo.proyecto_persona.model.Perfil;
import com.gustavo.proyecto_persona.repository.PerfilRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public List<Perfil> getPersonas() {
        return perfilRepository.findAll();
    }

    public void guardarPersona(PerfilAGuardarDto persona) {

        Perfil perfilAGuardar = new Perfil();
        perfilAGuardar.setNombre(persona.getNombre());

        perfilRepository.save(perfilAGuardar);
    }

    public void actualizarPersona(PerfilAEditarDto persona) throws EntityNotFoundException {

        Optional<Perfil> perfilEncontradaOp = perfilRepository.findById(persona.getId());

        if (perfilEncontradaOp.isEmpty()) {
            throw new EntityNotFoundException("No se pudo actualizar los datos, no se encontro a la persona. ");
        }

        Perfil perfilEncontrado = perfilEncontradaOp.get();

        perfilEncontrado.setNombre(persona.getNombre());
        

        perfilRepository.save(perfilEncontrado);
    }

    public Perfil getPersonaPorId(Long id) throws EntityNotFoundException {

        Optional<Perfil> persona = perfilRepository.findById(id);

        if (persona.isEmpty()) {
            throw new EntityNotFoundException("Persona no encontrada en la bd.");
        }

        return persona.get();
    }

    public void borrarPersona(Long id) throws EntityNotFoundException {

        boolean existePersona = perfilRepository.existsById(id);

        if (!existePersona) {
            throw new EntityNotFoundException("No se encontro el id , no se pudo borrar la persona.");
        }

        perfilRepository.deleteById(id);
    }

}
