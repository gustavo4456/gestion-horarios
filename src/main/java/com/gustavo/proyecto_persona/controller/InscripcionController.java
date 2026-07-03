package com.gustavo.proyecto_persona.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.gustavo.proyecto_persona.model.Inscripcion;
import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.model.Persona;
import com.gustavo.proyecto_persona.service.InscripcionService;
import com.gustavo.proyecto_persona.service.MateriaService;
import com.gustavo.proyecto_persona.service.PersonaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/inscripcion")
public class InscripcionController {

    private final PersonaService personaService;
    private final InscripcionService inscripcionService;
    private final MateriaService materiaService;

    public InscripcionController(PersonaService personaService, InscripcionService inscripcionService,
            MateriaService materiaService) {
        this.personaService = personaService;
        this.inscripcionService = inscripcionService;
        this.materiaService = materiaService;
    }

    @GetMapping("/menu")
    public String mostrarMenu() {
        return "inscripcion/menu";
    }

    @GetMapping("/nueva")
    public String mostrarFormNuevaInscripcio(Model model) {

        List<Materia> materias = materiaService.getMaterias();
        model.addAttribute("listadoMaterias", materias);

        carga(true, model);

        return "inscripcion/nueva";
    }

    @PostMapping("/guardar")
    public String guardarInscripcio(@Valid @ModelAttribute Inscripcion inscripcion, BindingResult result,
            @RequestParam(required = false) List<Long> seleccion, Model model) {

        if (result.hasErrors()) {

            List<Materia> materias = materiaService.getMaterias();
            model.addAttribute("listadoMaterias", materias);

            carga(false, model);

            return "inscripcion/nueva";
        }

        inscripcionService.guardarInscripcion(inscripcion, seleccion);

        model.addAttribute("msj", "Se guardo la inscripción en la BD.");

        return "mensaje";
    }

    @GetMapping("/filtro-materia")
    public String filtrar(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer anioCursadaFiltro, Model model) {

        if (nombre.isBlank() && anioCursadaFiltro != null) {

            List<Materia> materias = materiaService.buscarPorAnioCursada(anioCursadaFiltro);
            model.addAttribute("listadoMaterias", materias);

            carga(true, model);

            return "inscripcion/nueva";
        }

        if (!nombre.isBlank() && anioCursadaFiltro == null) {

            List<Materia> materias = materiaService.buscarPorNombre(nombre);
            model.addAttribute("listadoMaterias", materias);

            carga(true, model);

            return "inscripcion/nueva";
        }

        if (!nombre.isBlank() && anioCursadaFiltro != null) {

            List<Materia> materias = materiaService.buscarPorNombreYAanioCursada(nombre, anioCursadaFiltro);
            model.addAttribute("listadoMaterias", materias);

            carga(true, model);

            return "inscripcion/nueva";
        }

        model.addAttribute("faltanLLenarCampos", true);

        carga(true, model);

        return "inscripcion/nueva";
    }

    @GetMapping("/filtro-materia-editar")
    public String filtrarEditar(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer anioCursadaFiltro, @RequestParam Long idFiltro, Model model) {

        Inscripcion ins = inscripcionService.getInscripcionPorId(idFiltro);

        if (nombre.isBlank() && anioCursadaFiltro != null) {

            List<Materia> materias = materiaService.buscarPorAnioCursada(anioCursadaFiltro);
            model.addAttribute("listadoMaterias", materias);

            model.addAttribute("inscripcion", ins);

            carga(false, model);

            return "inscripcion/editar";
        }

        if (!nombre.isBlank() && anioCursadaFiltro == null) {

            List<Materia> materias = materiaService.buscarPorNombre(nombre);
            model.addAttribute("listadoMaterias", materias);

            model.addAttribute("inscripcion", ins);

            carga(false, model);

            return "inscripcion/editar";
        }

        if (!nombre.isBlank() && anioCursadaFiltro != null) {

            List<Materia> materias = materiaService.buscarPorNombreYAanioCursada(nombre, anioCursadaFiltro);
            model.addAttribute("listadoMaterias", materias);

            model.addAttribute("inscripcion", ins);

            carga(false, model);

            return "inscripcion/editar";
        }

        List<Materia> materias = materiaService.getMaterias();
        model.addAttribute("listadoMaterias", materias);

        model.addAttribute("inscripcion", ins);

        carga(false, model);

        return "inscripcion/editar";
    }

    @GetMapping("/listado")
    public String mostrarListado(Model model) {

        List<Inscripcion> inscripciones = inscripcionService.getInscripciones();

        model.addAttribute("inscripciones", inscripciones);

        return "inscripcion/listado";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, Model model) {

        inscripcionService.borrarInscripcion(id);

        model.addAttribute("msj", "Se ha eliminado la inscripción.");

        return "mensaje";
    }

    @GetMapping("/{id}/editar")
    public String mostrarEditar(@PathVariable Long id, Model model) {

        Inscripcion inscripcion = inscripcionService.getInscripcionPorId(id);
        List<Materia> materias = materiaService.getMaterias();

        model.addAttribute("inscripcion", inscripcion);
        model.addAttribute("listadoMaterias", materias);

        carga(false, model);

        return "inscripcion/editar";
    }

    @PostMapping("/editar")
    public String editar(@Valid @ModelAttribute Inscripcion inscripcion,
            @RequestParam(required = false) List<Long> seleccion,
            BindingResult result, Model model) {

        if (result.hasErrors()) {

            List<Materia> materias = materiaService.getMaterias();
            model.addAttribute("listadoMaterias", materias);
            carga(false, model);

            return "inscripcion/editar";
        }

        inscripcionService.actualizarInscripcion(inscripcion, seleccion);

        model.addAttribute("msj", "Se actualizaron los datos de la inscripción.");

        return "mensaje";
    }

    // Metodos de soporte
    private void carga(boolean conInscripcion, Model model) {

        if (conInscripcion) {
            model.addAttribute("inscripcion", new Inscripcion());
        }

        List<Persona> personas = personaService.getPersonas();
        model.addAttribute("personas", personas);

        model.addAttribute("aniosLectivo",
                IntStream.rangeClosed(1950, LocalDate.now().getYear()).boxed().toList().reversed());

    }

}
