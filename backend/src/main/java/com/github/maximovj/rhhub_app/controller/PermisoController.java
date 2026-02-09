package com.github.maximovj.rhhub_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maximovj.rhhub_app.dto.request.PermisoRequest;
import com.github.maximovj.rhhub_app.service.PermisoService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/v1/permisos")
@AllArgsConstructor
@Slf4j
public class PermisoController {

    private final PermisoService service;

    @GetMapping("")
    public ResponseEntity<?> getPermisos() {
        return this.service.verTodosPermisos();
    }
    
    @GetMapping("/{permiso_id}")
    public ResponseEntity<?> getVerDetallePermiso(
        @PathVariable(name = "permiso_id") Long permisoId
    ) {
        log.info("getVerDetallePermiso recibido: {}", permisoId);
        return this.service.verUnPermiso(permisoId);
    }
    
    @PostMapping("")
    public ResponseEntity<?> postCrearPermiso(
        @RequestBody PermisoRequest req
    ) {
        log.info("postCrearPermiso recibido: {}", req.toString()); 
        return this.service.crearUnPermiso(req);
    }

    @PutMapping("/{permiso_id}")
    public ResponseEntity<?> putActualizarPermiso(
        @PathVariable(name = "permiso_id") Long permisoId,
        @RequestBody PermisoRequest req
    ) {
        log.info("putActualizarPermiso recibido: {} / {}", permisoId, req.toString()); 
        return this.service.actualizarUnPermiso(permisoId, req);
    }

    @DeleteMapping("/{permiso_id}")
    public ResponseEntity<?> deleteEliminarPermiso(
        @PathVariable(name = "permiso_id") Long permisoId
    ) {
        log.info("deleteEliminarPermiso recibido: {}", permisoId); 
        return this.service.eliminarUnPermiso(permisoId);
    }

}
