package com.gustavo.proyecto_persona.controller.rest;

import com.gustavo.proyecto_persona.dto.rest.MateriaDto;
import com.gustavo.proyecto_persona.dto.rest.MateriaMapper;
import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.service.MateriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/materia")
public class MateriaRestController {

    private final MateriaService materiaService;

    public MateriaRestController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    //Devolver datos


    @GetMapping("/{id}")
    public ResponseEntity<MateriaDto> getMateriasPorId(@PathVariable Long id, Principal principal) {

        Materia materia = materiaService.getMateriaPorId(id, principal.getName());

        MateriaDto materiaDto = MateriaMapper.toDto(materia);

        return ResponseEntity.ok(materiaDto);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Set<MateriaDto>> getMaterias(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer anioCursada,
            Principal principal) {

        String username = principal.getName();
        List<Materia> materias;

        if (nombre != null && anioCursada != null) {
            materias = materiaService.buscarPorNombreYAanioCursada(nombre, anioCursada, username);
        } else if (nombre != null) {
            materias = materiaService.buscarPorNombre(nombre, username);
        } else if (anioCursada != null) {
            materias = materiaService.buscarPorAnioCursada(anioCursada, username);
        } else {
            materias = materiaService.getMaterias(username);
        }

        Set<MateriaDto> materiasDto = materias.stream()
                .map(MateriaMapper::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(materiasDto);
    }

    //Modificar cosas

    @PostMapping("/nueva")
    public ResponseEntity<String> guardarMateria(@RequestBody @Valid Materia materia, Principal principal) {

        materiaService.guardarMateria(materia, principal.getName());

        return ResponseEntity.ok("Se guardo la materia en la bd.");
    }

    @PatchMapping("/actualizar")
    public ResponseEntity<String> actualizarMateria(@RequestBody @Valid Materia materia, Principal principal) {


        materiaService.actualizarMateria(materia, principal.getName());

        return ResponseEntity.ok("Se actualizo la materia en la bd.");
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrarMateria(@PathVariable Long id, Principal principal) {

        materiaService.borrarMateria(id, principal.getName());

        return ResponseEntity.ok("Se borró la materia de la bd.");
    }
}
