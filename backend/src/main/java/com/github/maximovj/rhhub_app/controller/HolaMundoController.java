package com.github.maximovj.rhhub_app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/hola-mundo")
@CrossOrigin("*")
public class HolaMundoController {

    @GetMapping()
    public String getHolaMundo(@RequestParam String yo) {
        return new String("Hola mundo " + yo);
    }

}
