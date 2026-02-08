package com.github.maximovj.rhhub_app.controller;

import com.github.maximovj.rhhub_app.dto.request.UsuarioRequest;
import com.github.maximovj.rhhub_app.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/usuarios")
@AllArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

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

    @DeleteMapping("/{usuario_id}")
    public ResponseEntity<?> deleteUsuario(
        @PathVariable(required = true, name = "usuario_id") Long usuarioId
    ){
        return this.usuarioService.eliminarUsuarioEntity(usuarioId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putActualizarUsuario(
        @PathVariable Long id,
        @RequestBody @Valid UsuarioRequest req
    ) {
        return this.usuarioService.actualizarUsuarioEntity(id, req);
    }

}
