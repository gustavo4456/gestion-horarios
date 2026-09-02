package com.gustavo.proyecto_persona.service;

import java.util.List;
import java.util.Optional;

import com.gustavo.proyecto_persona.dto.rest.PerfilAGuardarRDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.proyecto_persona.dto.web.PerfilAEditarDto;
import com.gustavo.proyecto_persona.dto.web.PerfilAGuardarDto;
import com.gustavo.proyecto_persona.model.Perfil;
import com.gustavo.proyecto_persona.model.Usuario;
import com.gustavo.proyecto_persona.repository.PerfilRepository;
import com.gustavo.proyecto_persona.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public PerfilService(PerfilRepository perfilRepository, UsuarioRepository usuarioRepository) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Perfil> getPersonas(String username) {
        return perfilRepository.findByUsuarioUsername(username);
    }

    public void guardarPersona(PerfilAGuardarDto persona, String username) {
        Usuario usuarioLogueado = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario de la sesión actual."));

        Perfil perfilAGuardar = new Perfil();
        perfilAGuardar.setNombre(persona.getNombre());
        perfilAGuardar.setUsuario(usuarioLogueado);

        perfilRepository.save(perfilAGuardar);
    }

    public void guardarPersonaRest(PerfilAGuardarRDto persona, String username) {
        Usuario usuarioLogueado = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario de la sesión actual."));

        Perfil perfilAGuardar = new Perfil();
        perfilAGuardar.setNombre(persona.getNombre());
        perfilAGuardar.setUsuario(usuarioLogueado);

        perfilRepository.save(perfilAGuardar);
    }

    public void actualizarPersona(PerfilAEditarDto persona, String username) throws EntityNotFoundException {
        Optional<Perfil> perfilEncontradaOp = perfilRepository.findByIdAndUsuarioUsername(persona.getId(), username);

        if (perfilEncontradaOp.isEmpty()) {
            throw new EntityNotFoundException(
                    "No se pudo actualizar los datos. No se encontró el perfil o no tenés permisos.");
        }

        Perfil perfilEncontrado = perfilEncontradaOp.get();
        perfilEncontrado.setNombre(persona.getNombre());

        perfilRepository.save(perfilEncontrado);
    }

    public Perfil getPersonaPorId(Long id, String username) throws EntityNotFoundException {
        Optional<Perfil> persona = perfilRepository.findByIdAndUsuarioUsername(id, username);

        if (persona.isEmpty()) {
            throw new EntityNotFoundException("Persona no encontrada en la base de datos o acceso denegado.");
        }

        return persona.get();
    }

    public void borrarPersona(Long id, String username) throws EntityNotFoundException {
        boolean existePersona = perfilRepository.existsByIdAndUsuarioUsername(id, username);

        if (!existePersona) {
            throw new EntityNotFoundException("No se pudo borrar. El perfil no existe o no te pertenece.");
        }

        perfilRepository.deleteById(id);
    }

}
