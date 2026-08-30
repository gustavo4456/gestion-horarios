package com.gustavo.proyecto_persona.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public String manejarEntidadNoEncontrada(EntityNotFoundException ex, Model model) {
        model.addAttribute("msj", ex.getMessage());
        return "mensaje";
    }

    @ExceptionHandler(Exception.class)
    public String menejarErrorGeneral(Exception ex, Model model) {
        model.addAttribute("msj", ex.getMessage());
        return "mensaje";
    }
}
