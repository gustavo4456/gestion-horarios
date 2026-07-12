package com.gustavo.proyecto_persona.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gustavo.proyecto_persona.dto.PerfilAEditarDto;
import com.gustavo.proyecto_persona.dto.PerfilAGuardarDto;
import com.gustavo.proyecto_persona.model.Perfil;
import com.gustavo.proyecto_persona.service.PerfilService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping("/listado")
    public String getListadoDePersonas(Model model) {

        List<Perfil> perfiles = perfilService.getPersonas();
        int cantidadPersonas = perfiles.size();

        model.addAttribute("listadoPersonas", perfiles);
        model.addAttribute("cantidadPersonas", cantidadPersonas);

        return "/perfil/listado-personas";

    }

    @GetMapping("/nueva")
    public String mostrarFormNuevaPersona(Model model) {

        model.addAttribute("persona", new PerfilAGuardarDto());

        return "/perfil/form-nueva-persona";
    }

    @PostMapping("/guardar")
    public String postGuardarPersona(@Valid @ModelAttribute("persona") PerfilAGuardarDto persona, BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "/perfil/form-nueva-persona";
        }

        model.addAttribute("msj", "Persona Guardada con exito.");

        perfilService.guardarPersona(persona);

        return "mensaje";
    }

    @GetMapping("/buscar")
    public String getBuscar() {
        return "/perfil/busqueda";
    }

    @GetMapping("/buscando")
    public String getBuscadoPorIdString(@RequestParam("idBusqueda") Long id, Model model) {

        Perfil persona = perfilService.getPersonaPorId(id);

        model.addAttribute("persona", persona);

        return "/perfil/resultado-busqueda";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarPersona(@PathVariable("id") Long id, Model model) {

        perfilService.borrarPersona(id);

        model.addAttribute("msj", "Usuario Eliminado.");

        return "mensaje";
    }

    @GetMapping("/{id}/form-editar")
    public String mostrarFormEditar(@PathVariable("id") Long idPersona, Model model) {

        Perfil perfil = perfilService.getPersonaPorId(idPersona);

        PerfilAEditarDto perfilAEditar = new PerfilAEditarDto();
        perfilAEditar.setId(perfil.getId());
        perfilAEditar.setNombre(perfil.getNombre());

        model.addAttribute("persona", perfilAEditar);

        return "/perfil/form-editar-persona";
    }

    @PostMapping("/editar")
    public String postEditarPersona(@Valid @ModelAttribute("persona") PerfilAEditarDto persona, BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "/perfil/form-editar-persona";
        }

        perfilService.actualizarPersona(persona);

        model.addAttribute("msj", "Los Datos de la Persona fueron actualizados.");

        return "mensaje";
    }

}
