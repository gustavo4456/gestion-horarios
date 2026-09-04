package com.gustavo.proyecto_persona.controller.rest;

import com.gustavo.proyecto_persona.model.Usuario;
import com.gustavo.proyecto_persona.service.RegistroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
public class RegistroRestController {

    private final RegistroService registroService;

    public RegistroRestController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @PostMapping("/registro")
    public ResponseEntity<String> guardarUsuario(@RequestBody @Valid Usuario usuario) {

        registroService.guardarUsuario(usuario);

        return ResponseEntity.ok("Se creó un nuevo usuario.");
    }
}
