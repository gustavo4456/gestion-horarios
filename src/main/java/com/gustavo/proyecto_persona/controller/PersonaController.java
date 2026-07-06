package com.gustavo.proyecto_persona.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gustavo.proyecto_persona.dto.PersonaAEditarDto;
import com.gustavo.proyecto_persona.dto.PersonaAGuardarDto;
import com.gustavo.proyecto_persona.model.Persona;
import com.gustavo.proyecto_persona.service.PersonaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/persona")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping("/listado")
    public String getListadoDePersonas(Model model) {

        List<Persona> personas = personaService.getPersonas();
        int cantidadPersonas = personas.size();

        model.addAttribute("listadoPersonas", personas);
        model.addAttribute("cantidadPersonas", cantidadPersonas);

        return "/persona/listado-personas";

    }

    @GetMapping("/nueva")
    public String mostrarFormNuevaPersona(Model model) {

        model.addAttribute("persona", new PersonaAGuardarDto());

        return "/persona/form-nueva-persona";
    }

    @PostMapping("/guardar")
    public String postGuardarPersona(@Valid @ModelAttribute("persona") PersonaAGuardarDto persona, BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "/persona/form-nueva-persona";
        }

        model.addAttribute("msj", "Persona Guardada con exito.");

        personaService.guardarPersona(persona);

        return "mensaje";
    }

    @GetMapping("/buscar")
    public String getBuscar() {
        return "/persona/busqueda";
    }

    @GetMapping("/buscando")
    public String getBuscadoPorIdString(@RequestParam("idBusqueda") Long id, Model model) {

        Persona persona = personaService.getPersonaPorId(id);

        model.addAttribute("persona", persona);

        return "/persona/resultado-busqueda";
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarPersona(@PathVariable("id") Long id, Model model) {

        personaService.borrarPersona(id);

        model.addAttribute("msj", "Usuario Eliminado.");

        return "mensaje";
    }

    @GetMapping("/{id}/form-editar")
    public String mostrarFormEditar(@PathVariable("id") Long idPersona, Model model) {

        Persona persona = personaService.getPersonaPorId(idPersona);

        PersonaAEditarDto personaAEditar = new PersonaAEditarDto();
        personaAEditar.setId(persona.getId());
        personaAEditar.setNombre(persona.getNombre());
        personaAEditar.setApellido(persona.getApellido());
        personaAEditar.setEdad(persona.getEdad());
        personaAEditar.setDni(persona.getDni());

        model.addAttribute("persona", personaAEditar);

        return "/persona/form-editar-persona";
    }

    @PostMapping("/editar")
    public String postEditarPersona(@Valid @ModelAttribute("persona") PersonaAEditarDto persona, BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "/persona/form-editar-persona";
        }

        personaService.actualizarPersona(persona);

        model.addAttribute("msj", "Los Datos de la Persona fueron actualizados.");

        return "mensaje";
    }

}
