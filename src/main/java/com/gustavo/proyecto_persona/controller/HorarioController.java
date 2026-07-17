package com.gustavo.proyecto_persona.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gustavo.proyecto_persona.enums.DiasSemana;
import com.gustavo.proyecto_persona.enums.TipoMateria;
import com.gustavo.proyecto_persona.model.Horario;
import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.service.HorarioService;
import com.gustavo.proyecto_persona.service.MateriaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/horario")
public class HorarioController {

    private final HorarioService horarioService;
    private final MateriaService materiaService;

    public HorarioController(HorarioService horarioService, MateriaService materiaService) {
        this.horarioService = horarioService;
        this.materiaService = materiaService;
    }

    @GetMapping("/nuevo")
    public String mostrarFormNuevoHorario(Model model, Principal principal) {

        List<Materia> materias = materiaService.getMaterias(principal.getName());

        model.addAttribute("horario", new Horario());
        model.addAttribute("materias", materias);
        model.addAttribute("listaDias", DiasSemana.values());
        model.addAttribute("listaTiposMateria", List.of(TipoMateria.PRACTICA, TipoMateria.TEORIA));

        return "horario/form-nuevo-horario";
    }

    @PostMapping("/guardar")
    public String guardarHorario(@Valid @ModelAttribute Horario horario, BindingResult result, Model model,
            Principal principal) {

        if (result.hasErrors()) {
            List<Materia> materias = materiaService.getMaterias(principal.getName());

            model.addAttribute("materias", materias);
            model.addAttribute("listaDias", DiasSemana.values());
            model.addAttribute("listaTiposMateria", List.of(TipoMateria.PRACTICA, TipoMateria.TEORIA));

            return "/horario/form-nuevo-horario";
        }

        // Guardamos de forma segura pasándole el username
        horarioService.guardarHorario(horario, principal.getName());

        // Recuperamos el horario con validación de dueño para la pantalla de éxito
        Horario horarioBD = horarioService.getHorarioPorId(horario.getId(), principal.getName());

        model.addAttribute("horarioBD", horarioBD);
        model.addAttribute("urlRetorno", "/horario/listado");

        return "horario/resultado-guardar";
    }

    @GetMapping("/listado")
    public String mostrarFormularioHorarios(Model model, Principal principal) {

        List<Horario> horarios = horarioService.getHorarios(principal.getName());

        model.addAttribute("horarios", horarios);

        return "horario/listado-completo-horario";
    }

    @GetMapping("/filtro")
    public String filtro(@RequestParam("filtro") Long anioCursada, Model model, Principal principal) {

        List<Horario> horarios = horarioService.getHorariosPorAnioCursada(anioCursada, principal.getName());

        model.addAttribute("horarios", horarios);
        model.addAttribute("anioCursada", anioCursada);

        return "horario/resultado-filtro";
    }

    @GetMapping("/buscar")
    public String mostrarFormBuscar() {

        return "horario/busqueda";
    }

    @GetMapping("/buscando")
    public String busqueda(@RequestParam Long id, Model model, Principal principal) {

        Horario horario = horarioService.getHorarioPorId(id, principal.getName());
        model.addAttribute("horario", horario);

        return "horario/resultado-busqueda";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, Model model, Principal principal) {

        horarioService.borrarHorario(id, principal.getName());

        model.addAttribute("msj", "Se elimino el horario de la bd.");
        model.addAttribute("urlRetorno", "/horario/listado");

        return "mensaje";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormEditar(@PathVariable Long id, Model model, Principal principal) {

        Horario horarioDb = horarioService.getHorarioPorId(id, principal.getName());

        List<Materia> materias = materiaService.getMaterias(principal.getName());

        model.addAttribute("materias", materias);
        model.addAttribute("listaDias", DiasSemana.values());
        model.addAttribute("listaTiposMateria", List.of(TipoMateria.PRACTICA, TipoMateria.TEORIA));

        model.addAttribute("horarioDb", horarioDb);

        return "horario/form-editar";
    }

    @PostMapping("/editar")
    public String editar(@Valid @ModelAttribute("horarioDb") Horario horario, BindingResult result, Model model,
            Principal principal) {

        if (result.hasErrors()) {

            List<Materia> materias = materiaService.getMaterias(principal.getName());

            model.addAttribute("materias", materias);
            model.addAttribute("listaDias", DiasSemana.values());
            model.addAttribute("listaTiposMateria", List.of(TipoMateria.PRACTICA, TipoMateria.TEORIA));

            return "horario/form-editar";
        }

        model.addAttribute("msj", "Se actualizaron los datos del horario.");
        model.addAttribute("urlRetorno", "/horario/listado");

        horarioService.actualizarHorario(horario, principal.getName());

        return "mensaje";
    }
}
