package com.gustavo.proyecto_persona.controller.rest;

import com.gustavo.proyecto_persona.dto.rest.PerfilAEditarRDto;
import com.gustavo.proyecto_persona.dto.rest.PerfilAGuardarRDto;
import com.gustavo.proyecto_persona.dto.rest.PerfilDto;
import com.gustavo.proyecto_persona.dto.rest.PerfilMapper;
import com.gustavo.proyecto_persona.model.Perfil;
import com.gustavo.proyecto_persona.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/perfil")
public class PerfilRestController {

    private final PerfilService perfilService;

    public PerfilRestController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<PerfilDto>> getPerfiles(Principal principal) {

        List<Perfil> perfiles = perfilService.getPersonas(principal.getName());


        List<PerfilDto> perfilesDto = perfiles.stream()
                .map(PerfilMapper::toDto)
                .collect(Collectors.toList());


        return ResponseEntity.ok(perfilesDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilDto> getPerfilPorId(@PathVariable Long id, Principal principal) {

        PerfilDto perfilDto = PerfilMapper.toDto(perfilService.getPersonaPorId(id, principal.getName()));

        return ResponseEntity.ok(perfilDto);
    }

    @PostMapping("/nuevo")
    public ResponseEntity<String> crearPerfil(@RequestBody @Valid PerfilAGuardarRDto perfilAGuardarDto, Principal principal) {

        perfilService.guardarPersonaRest(perfilAGuardarDto, principal.getName());

        return ResponseEntity.ok("Se creo el Perfil correctamente.");
    }

    @PatchMapping("/actualizar")
    public ResponseEntity<String> actualizarPerfil(@RequestBody @Valid PerfilAEditarRDto perfilAEditarRDto, Principal principal) {

        perfilService.actualizarPersonaRest(perfilAEditarRDto, principal.getName());

        return ResponseEntity.ok("Se actualizar los datos del perfil.");
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrarPerfil(@PathVariable Long id, Principal principal) {

        perfilService.borrarPersona(id, principal.getName());

        return ResponseEntity.ok("Se elimino un perfil.");
    }

}
