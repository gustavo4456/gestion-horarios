package com.gustavo.proyecto_persona.controller.rest;

import com.gustavo.proyecto_persona.dto.rest.PerfilDto;
import com.gustavo.proyecto_persona.dto.rest.PerfilMapper;
import com.gustavo.proyecto_persona.model.Perfil;
import com.gustavo.proyecto_persona.service.PerfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PerfilRestController {

    private final PerfilService perfilService;

    public PerfilRestController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping("/perfiles")
    public ResponseEntity<List<PerfilDto>> getPerfiles(Principal principal) {

        List<Perfil> perfiles = perfilService.getPersonas(principal.getName());


        List<PerfilDto> perfilesDto = perfiles.stream()
                .map(perfil -> PerfilMapper.toDto(perfil))
                .collect(Collectors.toList());


        return ResponseEntity.ok(perfilesDto);
    }

}
