package com.gustavo.proyecto_persona.controller.web;

import java.security.Principal;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gustavo.proyecto_persona.dto.HorarioDto;
import com.gustavo.proyecto_persona.enums.DiasSemana;
import com.gustavo.proyecto_persona.model.Inscripcion;
import com.gustavo.proyecto_persona.model.Perfil;
import com.gustavo.proyecto_persona.service.InscripcionService;
import com.gustavo.proyecto_persona.service.PerfilService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrincipalController {

        private final PerfilService personaService;
        private final InscripcionService inscripcionService;

        public PrincipalController(PerfilService personaService, InscripcionService inscripcionService) {
                this.personaService = personaService;
                this.inscripcionService = inscripcionService;
        }

        @GetMapping("/home")
        public String getIndex() {
                return "index";
        }

        @GetMapping("/horarios")
        public String getHorarios(Model model, Principal principal) {

                List<Perfil> personas = personaService.getPersonas(principal.getName());

                model.addAttribute("personas", personas);

                return "principal/horarios";
        }

        @GetMapping("/filtro-horarios")
        public String filtroHorarios(@RequestParam Long persona, @RequestParam Integer anioLectivo, Model model,
                        Principal principal) {

                List<Inscripcion> inscripciones = inscripcionService.getInscripcionesPorPersonaYAnioLectivo(persona,
                                anioLectivo, principal.getName());

                Map<Integer, List<HorarioDto>> horariosPorAnio = inscripciones.stream()
                                .flatMap(inscripcion -> inscripcion.getMaterias().stream())
                                .flatMap(materia -> materia.getHorarios().stream()
                                                .map(horario -> new HorarioDto(
                                                                materia.getNombre(),
                                                                materia.getAnioCursada(),
                                                                horario.getDiasSemana(),
                                                                horario.getHoraInicio(),
                                                                horario.getHoraFin(),
                                                                horario.getTipoMateria())))
                                .collect(Collectors.groupingBy(
                                                HorarioDto::getAnio,
                                                TreeMap::new,
                                                Collectors.collectingAndThen(
                                                                Collectors.toList(),
                                                                lista -> lista.stream()
                                                                                .sorted(Comparator.comparing(
                                                                                                HorarioDto::getHoraInicio))
                                                                                .toList())));

                List<Perfil> personas = personaService.getPersonas(principal.getName());

                Map<Integer, List<String>> horasPorAnioMap = horariosPorAnio.entrySet().stream()
                                .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                entry -> entry.getValue().stream()
                                                                .map(h -> h.getHoraInicio() + " - " + h.getHoraFin())
                                                                .distinct()
                                                                .toList()));

                model.addAttribute("personas", personas);
                model.addAttribute("horariosPorAnio", horariosPorAnio);
                model.addAttribute("horasPorAnioMap", horasPorAnioMap); // 🚀 Enviamos el mapa a la vista
                model.addAttribute("diasSemana", DiasSemana.values());
                model.addAttribute("anioLectivo", anioLectivo);

                return "principal/horarios";
        }

}
