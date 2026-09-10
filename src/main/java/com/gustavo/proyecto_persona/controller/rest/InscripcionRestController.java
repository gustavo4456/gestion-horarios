package com.gustavo.proyecto_persona.controller.rest;

import com.gustavo.proyecto_persona.dto.rest.InscripcionDto;
import com.gustavo.proyecto_persona.dto.rest.InscripcionMapper;
import com.gustavo.proyecto_persona.dto.rest.InscripcionSimpleDto;
import com.gustavo.proyecto_persona.dto.rest.InscripcionSimpleDtoMapper;
import com.gustavo.proyecto_persona.model.Inscripcion;
import com.gustavo.proyecto_persona.service.InscripcionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inscripcion")
public class InscripcionRestController {

    private final InscripcionService inscripcionService;

    public InscripcionRestController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    // Devolver datos

    @GetMapping("/buscar/{id}")
    public ResponseEntity<InscripcionDto> getInscripcion(@PathVariable Long id, Principal principal) {

        Inscripcion inscripcion = inscripcionService.getInscripcionPorId(id, principal.getName());

        InscripcionDto inscripcionDto = InscripcionMapper.toDto(inscripcion);

        return ResponseEntity.ok(inscripcionDto);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<InscripcionDto>> getInscripciones(@RequestParam(required = false) Long idPersona,
                                                                 @RequestParam(required = false) Integer anioLectivo,
                                                                 Principal principal) {

        List<Inscripcion> inscripciones;

        if (idPersona != null && anioLectivo == null) {

            inscripciones = inscripcionService.getInscripcionesPorPersona(idPersona, principal.getName());

        } else if (idPersona == null && anioLectivo != null) {

            inscripciones = inscripcionService.getInscripcionesPorAnioLectivo(anioLectivo, principal.getName());

        } else if (idPersona != null && anioLectivo != null) {

            inscripciones = inscripcionService.getInscripcionesPorPersonaYAnioLectivo(idPersona, anioLectivo,
                    principal.getName());

        } else {
            inscripciones = inscripcionService.getInscripciones(principal.getName());
        }

        List<InscripcionDto> inscripcionesDto = inscripciones.stream()
                .map(InscripcionMapper::toDto)
                .toList();

        return ResponseEntity.ok(inscripcionesDto);
    }

    // Modificar cosas

    //seleccion: son los id de las materias, Perfil solo necesita el id, el mapper se encarga de asiganrlo
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarInscripcion(@RequestBody @Valid InscripcionSimpleDto inscripcionSimpleDto,
                                                     @RequestParam(required = false) List<Long> seleccion,
                                                     Principal principal) {

        Inscripcion inscripcion = InscripcionSimpleDtoMapper.toClass(inscripcionSimpleDto);

        inscripcionService.guardarInscripcion(inscripcion, seleccion, principal.getName());

        return ResponseEntity.ok("Se guardo la inscripcion.");
    }

    @PatchMapping("/actualizar")
    public ResponseEntity<String> actualizarInscripcion(@RequestBody @Valid InscripcionSimpleDto inscripcionSimpleDto,
                                                        @RequestParam(required = false) List<Long> seleccion,
                                                        Principal principal) {

        Inscripcion inscripcion = InscripcionSimpleDtoMapper.toClass(inscripcionSimpleDto);

        inscripcionService.actualizarInscripcion(inscripcion, seleccion, principal.getName());

        return ResponseEntity.ok("Se guardo la inscripcion.");
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrarInscripcion(@PathVariable Long id, Principal principal) {

        inscripcionService.borrarInscripcion(id, principal.getName());

        return ResponseEntity.ok("Se borro la inscripcion.");
    }
}


