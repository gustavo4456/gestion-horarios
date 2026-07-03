package com.gustavo.proyecto_persona.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrincipalController {

    @GetMapping("/home")
    public String getIndex() {
        return "index";
    }
}
