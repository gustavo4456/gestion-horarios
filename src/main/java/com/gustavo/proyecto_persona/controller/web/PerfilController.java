package com.gustavo.proyecto_persona.controller.web;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gustavo.proyecto_persona.dto.web.PerfilAEditarDto;
import com.gustavo.proyecto_persona.dto.web.PerfilAGuardarDto;
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
    public String getListadoDePersonas(Model model, Principal principal) {

        List<Perfil> perfiles = perfilService.getPersonas(principal.getName());
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
    public String postGuardarPersona(@Valid @ModelAttribute("persona") PerfilAGuardarDto persona,
            BindingResult result,
            Model model,
            Principal principal) {

        if (result.hasErrors()) {
            return "/perfil/form-nueva-persona";
        }

        model.addAttribute("msj", "Persona Guardada con exito.");
        model.addAttribute("urlRetorno", "/perfil/listado");

        // Guardamos pasándole el username de la sesión
        perfilService.guardarPersona(persona, principal.getName());

        return "mensaje";
    }

    @GetMapping("/buscar")
    public String getBuscar() {
        return "/perfil/busqueda";
    }

    @GetMapping("/buscando")
    public String getBuscadoPorIdString(@RequestParam("idBusqueda") Long id,
            Model model,
            Principal principal) {

        // Buscamos asegurando que pertenezca al usuario de la sesión
        Perfil persona = perfilService.getPersonaPorId(id, principal.getName());

        model.addAttribute("persona", persona);

        return "/perfil/resultado-busqueda";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarPersona(@PathVariable("id") Long id,
            Model model,
            Principal principal) {

        // Borrado seguro que valida pertenencia antes de eliminar
        perfilService.borrarPersona(id, principal.getName());

        model.addAttribute("msj", "Usuario Eliminado.");
        model.addAttribute("urlRetorno", "/perfil/listado");

        return "mensaje";
    }

    @GetMapping("/{id}/form-editar")
    public String mostrarFormEditar(@PathVariable("id") Long idPersona,
            Model model,
            Principal principal) {

        // Nos aseguramos que el perfil a editar sea del usuario activo
        Perfil perfil = perfilService.getPersonaPorId(idPersona, principal.getName());

        PerfilAEditarDto perfilAEditar = new PerfilAEditarDto();
        perfilAEditar.setId(perfil.getId());
        perfilAEditar.setNombre(perfil.getNombre());

        model.addAttribute("persona", perfilAEditar);
        model.addAttribute("urlRetorno", "/perfil/listado");

        return "/perfil/form-editar-persona";
    }

    @PostMapping("/editar")
    public String postEditarPersona(@Valid @ModelAttribute("persona") PerfilAEditarDto persona,
            BindingResult result,
            Model model,
            Principal principal) {

        if (result.hasErrors()) {
            return "/perfil/form-editar-persona";
        }

        // Actualizamos validando que sea el dueño
        perfilService.actualizarPersona(persona, principal.getName());

        model.addAttribute("msj", "Los Datos de la Persona fueron actualizados.");
        model.addAttribute("urlRetorno", "/perfil/listado");

        return "mensaje";
    }

}
