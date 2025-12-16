package com.github.maximovj.rhhub_app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class HolaMundoController {

    @GetMapping("/hola-mundo")
    public String getHolaMundo(@RequestParam String yo) {
        return new String("Hola mundo " + yo);
    }

}
