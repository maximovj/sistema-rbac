package com.github.maximovj.rhhub_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.maximovj.rhhub_app.dto.request.GrupoRequest;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.service.GrupoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/grupos")
@AllArgsConstructor
@Slf4j
public class GrupoController {

    private final GrupoService service;

    // !! Mostrar todos los grupos
    @GetMapping("")
    public ResponseEntity<?> getVerTodos(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        // Mostrar todos con paginación
        return this.service.verGrupos(page, size); 
    }

    // !! Buscar grupos
    @GetMapping("/q/busqueda")
    public ResponseEntity<?> getBusqueda(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @ModelAttribute GrupoEntity req
    ) {
        // Filtrar todos con paginación
        return this.service.busquedaPorPropiedades(page, size, req); 
    }

    // !! Ver un grupo por grupo_id
    @GetMapping("/{grupo_id}")
    public ResponseEntity<?> getVerUnGrupo(
        @PathVariable(name = "grupo_id") Long grupoId
    ) {
        return this.service.verGrupo(grupoId);
    }

    // !! Actualizar un grupo
    @PutMapping("/{grupo_id}")
    public ResponseEntity<?> putActualizarGrupo(
        @PathVariable(value = "grupo_id") Long grupoId,
        @RequestBody @Valid GrupoRequest req
    ) {
        log.info("putActualizarGrupo JSON recibido: {}", req.toString());
        return this.service.actualizarUnGrupo(grupoId, req);
    }

    // !! Crear un grupo
    @PostMapping("")
    public ResponseEntity<?> postCrearGrupo(
        @RequestBody() @Valid GrupoRequest req
    ) {
        log.info("putActualizarGrupo JSON recibido: {}", req.toString());
        return this.service.crearUnGrupo(req);
    }

    // !! Eliminar un grupo
    @DeleteMapping("/{grupo_id}")
    public ResponseEntity<?> deleteEliminarGrupo(
        @PathVariable(value = "grupo_id") Long grupoId
    ) {
        log.info("deleteEliminarUnGrupo grupo_id recibido: {}", grupoId);
        return this.service.eliminarUnGrupo(grupoId);
    }
    
}
