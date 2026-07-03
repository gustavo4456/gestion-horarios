package com.gustavo.proyecto_persona.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gustavo.proyecto_persona.enums.TipoMateria;
import com.gustavo.proyecto_persona.model.Materia;
import com.gustavo.proyecto_persona.service.MateriaService;

import jakarta.validation.Valid;

@Controller()
@RequestMapping("/materia")
public class MateriaController {

    public final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping("/menu")
    public String getMenu() {
        return "/materia/menu";
    }

    @GetMapping("/listado")
    public String getListadoMaterias(Model model) {

        List<Materia> materias = materiaService.getMaterias();

        model.addAttribute("listadoMaterias", materias);

        return "/materia/listado-materias";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaMateria(Model model) {

        model.addAttribute("tiposMaterias", List.of(TipoMateria.ANUAL, TipoMateria.CUATRIMESTRAL));

        model.addAttribute("nuevaMateria", new Materia());

        return "/materia/form-nueva-materia";
    }

    @PostMapping("/guardar")
    public String guardarMateria(@Valid @ModelAttribute("nuevaMateria") Materia materia,
            BindingResult result, Model model) {

        if (result.hasErrors()) {

            model.addAttribute("tiposMaterias", List.of(TipoMateria.ANUAL, TipoMateria.CUATRIMESTRAL));

            return "/materia/form-nueva-materia";
        }

        materiaService.guardarMateria(materia);

        model.addAttribute("msj", "La materia se guardo en la bd.");

        return "mensaje";
    }

    @GetMapping("/{id}/form-editar")
    public String mostrarFormEditar(@PathVariable("id") Long idMateria, Model model) {

        Materia materiabd = materiaService.getMateriaPorId(idMateria);

        model.addAttribute("tiposMaterias", List.of(TipoMateria.ANUAL, TipoMateria.CUATRIMESTRAL));
        model.addAttribute("materiabd", materiabd);

        return "/materia/form-editar-materia";
    }

    @PostMapping("/editar")
    public String editarMaterio(@Valid @ModelAttribute("materiabd") Materia materia, BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("materiabd", materia);

            model.addAttribute("tiposMaterias", List.of(TipoMateria.ANUAL, TipoMateria.CUATRIMESTRAL));
            return "materia/form-editar-materia";
        }

        model.addAttribute("msj", "Se actualizaron los datos de la materia correctamente.");

        materiaService.actualizarMateria(materia);
        return "mensaje";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarMateria(@PathVariable("id") Long idMateria, Model model) {

        materiaService.borrarMateria(idMateria);

        model.addAttribute("msj", "La materia fue eliminada de la bd");

        return "mensaje";
    }

    @GetMapping("/buscar")
    public String mostrarFormBuscar() {
        return "materia/busqueda";
    }

    @GetMapping("/buscando")
    public String buscarMateriaPorId(@RequestParam("id") Long id, Model model) {

        Materia materia = materiaService.getMateriaPorId(id);

        model.addAttribute("materia", materia);

        return "materia/resultado-busqueda";
    }

}
