package com.gustavo.proyecto_persona.controller.rest;

import com.gustavo.proyecto_persona.dto.rest.HorarioConMateriaDto;
import com.gustavo.proyecto_persona.dto.rest.HorarioConMateriaMapper;
import com.gustavo.proyecto_persona.model.Horario;
import com.gustavo.proyecto_persona.service.HorarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/horario")
public class HorarioRestController {

    private final HorarioService horarioService;

    public HorarioRestController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }


    // Devolver datos
    @GetMapping("/buscar")
    public ResponseEntity<Set<HorarioConMateriaDto>> getHorarios(Principal principal) {

        List<Horario> horarios = horarioService.getHorarios(principal.getName());

        Set<HorarioConMateriaDto> horarioConMateriaDtos = horarios.stream()
                .map(HorarioConMateriaMapper::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(horarioConMateriaDtos);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<HorarioConMateriaDto> getHorario(@PathVariable Long id, Principal principal) {

        Horario horario = horarioService.getHorarioPorId(id, principal.getName());

        HorarioConMateriaDto horarioConMateriaDto = HorarioConMateriaMapper.toDto(horario);

        return ResponseEntity.ok(horarioConMateriaDto);

    }

    @GetMapping("/buscar/porAnioCursada/{anioCursada}")
    public ResponseEntity<Set<HorarioConMateriaDto>> getHorariosPorAnioCursada(@PathVariable Long anioCursada, Principal principal) {

        List<Horario> horarios = horarioService.getHorariosPorAnioCursada(anioCursada, principal.getName());

        Set<HorarioConMateriaDto> horariosConMateriaDto = horarios.stream()
                .map(HorarioConMateriaMapper::toDto)
                .collect(Collectors.toSet());


        return ResponseEntity.ok(horariosConMateriaDto);

    }


    // Modificar cosas

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrarHorario(@PathVariable Long id, Principal principal) {

        horarioService.borrarHorario(id, principal.getName());

        return ResponseEntity.ok("Se borro el horario.");
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarHorario(@RequestBody @Valid Horario horario, Principal principal) {

        horarioService.guardarHorario(horario, principal.getName());

        return ResponseEntity.ok("Se guardo el horario.");
    }

    @PatchMapping("/actualizar")
    public ResponseEntity<String> actualizarHorario(@RequestBody @Valid Horario horario, Principal principal) {

        horarioService.actualizarHorario(horario, principal.getName());

        return ResponseEntity.ok("Se actualizo el horario.");
    }
}
