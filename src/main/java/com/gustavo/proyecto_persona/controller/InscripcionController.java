package com.gustavo.proyecto_persona.controller;

import java.security.Principal;

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
import com.gustavo.proyecto_persona.model.Perfil;
import com.gustavo.proyecto_persona.service.InscripcionService;
import com.gustavo.proyecto_persona.service.MateriaService;
import com.gustavo.proyecto_persona.service.PerfilService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/inscripcion")
public class InscripcionController {

    private final PerfilService perfilService;
    private final InscripcionService inscripcionService;
    private final MateriaService materiaService;

    public InscripcionController(PerfilService perfilService, InscripcionService inscripcionService,
            MateriaService materiaService) {
        this.perfilService = perfilService;
        this.inscripcionService = inscripcionService;
        this.materiaService = materiaService;
    }

    @GetMapping("/nueva")
    public String mostrarFormNuevaInscripcio(Model model, Principal principal) {

        List<Materia> materias = materiaService.getMaterias(principal.getName());
        model.addAttribute("listadoMaterias", materias);

        carga(true, model, principal);

        return "inscripcion/nueva";
    }

    @PostMapping("/guardar")
    public String guardarInscripcio(@Valid @ModelAttribute Inscripcion inscripcion, BindingResult result,
            @RequestParam(required = false) List<Long> seleccion, Model model, Principal principal) {

        if (result.hasErrors()) {

            List<Materia> materias = materiaService.getMaterias(principal.getName());
            model.addAttribute("listadoMaterias", materias);

            carga(false, model, principal);

            return "inscripcion/nueva";
        }

        inscripcionService.guardarInscripcion(inscripcion, seleccion, principal.getName());

        model.addAttribute("msj", "Se guardo la inscripción en la BD.");

        return "mensaje";
    }

    @GetMapping("/filtro-materia")
    public String filtrar(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer anioCursadaFiltro, Model model, Principal principal) {

        if (nombre.isBlank() && anioCursadaFiltro != null) {

            List<Materia> materias = materiaService.buscarPorAnioCursada(anioCursadaFiltro, principal.getName());
            model.addAttribute("listadoMaterias", materias);

            carga(true, model, principal);

            return "inscripcion/nueva";
        }

        if (!nombre.isBlank() && anioCursadaFiltro == null) {

            List<Materia> materias = materiaService.buscarPorNombre(nombre, principal.getName());
            model.addAttribute("listadoMaterias", materias);

            carga(true, model, principal);

            return "inscripcion/nueva";
        }

        if (!nombre.isBlank() && anioCursadaFiltro != null) {

            List<Materia> materias = materiaService.buscarPorNombreYAanioCursada(nombre, anioCursadaFiltro,
                    principal.getName());
            model.addAttribute("listadoMaterias", materias);

            carga(true, model, principal);

            return "inscripcion/nueva";
        }

        List<Materia> materias = materiaService.getMaterias(principal.getName());
        model.addAttribute("listadoMaterias", materias);

        carga(true, model, principal);

        return "inscripcion/nueva";
    }

    @GetMapping("/filtro-materia-editar")
    public String filtrarEditar(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer anioCursadaFiltro, @RequestParam Long idFiltro, Model model,
            Principal principal) {

        Inscripcion ins = inscripcionService.getInscripcionPorId(idFiltro, principal.getName());

        if (nombre.isBlank() && anioCursadaFiltro != null) {

            List<Materia> materias = materiaService.buscarPorAnioCursada(anioCursadaFiltro, principal.getName());
            model.addAttribute("listadoMaterias", materias);

            model.addAttribute("inscripcion", ins);

            carga(false, model, principal);

            return "inscripcion/editar";
        }

        if (!nombre.isBlank() && anioCursadaFiltro == null) {

            List<Materia> materias = materiaService.buscarPorNombre(nombre, principal.getName());
            model.addAttribute("listadoMaterias", materias);

            model.addAttribute("inscripcion", ins);

            carga(false, model, principal);

            return "inscripcion/editar";
        }

        if (!nombre.isBlank() && anioCursadaFiltro != null) {

            List<Materia> materias = materiaService.buscarPorNombreYAanioCursada(nombre, anioCursadaFiltro,
                    principal.getName());
            model.addAttribute("listadoMaterias", materias);

            model.addAttribute("inscripcion", ins);

            carga(false, model, principal);

            return "inscripcion/editar";
        }

        List<Materia> materias = materiaService.getMaterias(principal.getName());
        model.addAttribute("listadoMaterias", materias);

        model.addAttribute("inscripcion", ins);

        carga(false, model, principal);

        return "inscripcion/editar";
    }

    @GetMapping("/listado")
    public String mostrarListado(Model model, Principal principal) {

        List<Inscripcion> inscripciones = inscripcionService.getInscripciones(principal.getName());
        List<Perfil> personas = perfilService.getPersonas(principal.getName());

        model.addAttribute("personas", personas);

        model.addAttribute("inscripciones", inscripciones);

        return "inscripcion/listado";
    }

    @GetMapping("/filtro-inscripciones")
    public String filtroListado(@RequestParam(name = "persona", required = false) Long idPersona,
            @RequestParam(required = false) Integer anioLectivo,
            Model model, Principal principal) {

        if (idPersona == null && anioLectivo != null) {
            List<Inscripcion> inscripciones = inscripcionService.getInscripcionesPorAnioLectivo(anioLectivo,
                    principal.getName());

            model.addAttribute("inscripciones", inscripciones);
        } else if (idPersona != null && anioLectivo == null) {
            List<Inscripcion> inscripciones = inscripcionService.getInscripcionesPorPersona(idPersona,
                    principal.getName());

            model.addAttribute("inscripciones", inscripciones);
        } else if (idPersona != null && anioLectivo != null) {
            List<Inscripcion> inscripciones = inscripcionService.getInscripcionesPorPersonaYAnioLectivo(idPersona,
                    anioLectivo, principal.getName());

            model.addAttribute("inscripciones", inscripciones);
        } else {
            List<Inscripcion> inscripciones = inscripcionService.getInscripciones(principal.getName());

            model.addAttribute("inscripciones", inscripciones);
        }

        List<Perfil> personas = perfilService.getPersonas(principal.getName());

        model.addAttribute("personas", personas);

        return "inscripcion/listado";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, Model model, Principal principal) {

        inscripcionService.borrarInscripcion(id, principal.getName());

        model.addAttribute("msj", "Se ha eliminado la inscripción.");

        return "mensaje";
    }

    @GetMapping("/{id}/editar")
    public String mostrarEditar(@PathVariable Long id, Model model, Principal principal) {

        Inscripcion inscripcion = inscripcionService.getInscripcionPorId(id, principal.getName());
        List<Materia> materias = materiaService.getMaterias(principal.getName());

        model.addAttribute("inscripcion", inscripcion);
        model.addAttribute("listadoMaterias", materias);

        carga(false, model, principal);

        return "inscripcion/editar";
    }

    @PostMapping("/editar")
    public String editar(@Valid @ModelAttribute Inscripcion inscripcion,
            @RequestParam(required = false) List<Long> seleccion,
            BindingResult result, Model model, Principal principal) {

        if (result.hasErrors()) {

            List<Materia> materias = materiaService.getMaterias(principal.getName());
            model.addAttribute("listadoMaterias", materias);
            carga(false, model, principal);

            return "inscripcion/editar";
        }

        inscripcionService.actualizarInscripcion(inscripcion, seleccion, principal.getName());

        model.addAttribute("msj", "Se actualizaron los datos de la inscripción.");

        return "mensaje";
    }

    // Metodos de soporte
    private void carga(boolean conInscripcion, Model model, Principal principal) {

        if (conInscripcion) {
            model.addAttribute("inscripcion", new Inscripcion());
        }

        List<Perfil> personas = perfilService.getPersonas(principal.getName());
        model.addAttribute("personas", personas);

        model.addAttribute("aniosLectivo",
                IntStream.rangeClosed(1950, LocalDate.now().getYear()).boxed().toList().reversed());

    }

}
