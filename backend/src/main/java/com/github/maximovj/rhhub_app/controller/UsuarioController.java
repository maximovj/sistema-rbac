package com.github.maximovj.rhhub_app.controller;

import com.github.maximovj.rhhub_app.service.UsuarioService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/q/busqueda")
    public ResponseEntity<?> getBusqueda(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        return this.usuarioService.busqueda(page, size);
    }

    @GetMapping("/{usuario_id}")
    public ResponseEntity<?> verUsuario(
        @PathVariable(required = true, name = "usuario_id") Long usuarioId
    ) {
        return this.usuarioService.cargarUsuarioEntity(usuarioId);
    }

}
