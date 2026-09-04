package com.gustavo.proyecto_persona.service;

import com.gustavo.proyecto_persona.model.Usuario;
import com.gustavo.proyecto_persona.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void guardarUsuario(Usuario usuario) throws EntityNotFoundException {

        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {

            throw new EntityNotFoundException("El nombre de usuario ya está registrado.");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol("ROLE_USER");

        usuarioRepository.save(usuario);
    }
}
