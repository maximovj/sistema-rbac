package com.github.maximovj.rhhub_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maximovj.rhhub_app.dto.request.RolRequest;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.entity.RolEntity;
import com.github.maximovj.rhhub_app.exception.BusinessException;
import com.github.maximovj.rhhub_app.exception.ResourceNotFoundException;
import com.github.maximovj.rhhub_app.mapper.RolMapper;
import com.github.maximovj.rhhub_app.projection.RolProjection;
import com.github.maximovj.rhhub_app.repository.specification.RolSpecBuilder;
import com.github.maximovj.rhhub_app.service.RolService;
import com.github.maximovj.rhhub_app.util.ValidRequest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

import javax.swing.text.html.parser.Entity;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
@Slf4j
public class RolController {

    private final RolService service;

    // !! Ver todo los roles
    @GetMapping("")
    public ResponseEntity<?> getVerTodos(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        ApiResponse<RolProjection> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre"));
        return response.okPage("Lista de roles", this.service.mostrarTodos(pageable));
    }

    // !! Buscar roles
    @GetMapping("/q/buscar")
    public ResponseEntity<?> getBuscar(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @ModelAttribute RolRequest req
    ) {
        ApiResponse<RolProjection> response = new ApiResponse<>();
        Specification<RolEntity> spec = new RolSpecBuilder()
                                        .nombre(req.getNombre())
                                        .descripcion(req.getDescripcion())
                                        .build();
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre"));
        return response.okPage("Lista de roles", this.service.buscar(spec, pageable));
    }

    // !! Ver detalle de un rol
    @GetMapping("/{rol_id}")
    public ResponseEntity<?> getVerUnReol(
        @PathVariable(name = "rol_id") Long rolId
    ) {
        ValidRequest.requireNonNull(rolId, "El campo rol_id es obligatoria");
        log.info("getVerUnReol recibido: {}", rolId);

        RolEntity rol = this.service.findById(rolId);
        if(Objects.isNull(rol)){
            return ApiResponse.notFound("Rol con id(%d) no encontrada".formatted(rolId), null);
        }
        
        return ApiResponse.ok("Info de rol", RolMapper.toDto(rol));
    }

    // Actualizar un rol
    @PutMapping("/{rol_id}")
    public ResponseEntity<?> putActualizarRol(
        @PathVariable(name = "rol_id") Long rolId,
        @RequestBody RolRequest req
    )
    {
        ValidRequest.requireNonNull(req, "El cuerpo JSON es obligatoria");
        ValidRequest.requireNonNull(rolId, "El campo rol_id es obligatoria");
        log.info("putActualizarRol recibido: {}", req.toString());

        RolEntity rol = this.service.findById(rolId);
        if(Objects.isNull(rol)) {
            return ApiResponse.notFound("Rol con id(%d) no encontrada".formatted(rolId), null);
        }

        RolMapper.updateFromRequest(rol, req);
        RolEntity actualizada = this.service.update(rolId, rol);
        return ApiResponse.ok("Rol actualizada correctamente", RolMapper.toDto(actualizada));
    }

    // !! Crear un rol
    @PostMapping("")
    public ResponseEntity<?> postCrearRol(
        @RequestBody @Valid RolRequest req
    ) {
        ValidRequest.requireNonNull(req, "El cuerpo JSON es obligatoria");
        ValidRequest.requireNonNull(req.getNombre(), "El campo nombre es obligatoria");
        ValidRequest.requireNonNull(req.getDescripcion(), "El campo descripcion es obligatoria");
        log.info("putActualizarRol recibido: {}", req.toString());

        boolean existe = this.service.esoExiste(req.getNombre().trim());
        if(existe) {
            return ApiResponse.notFound("Rol %s existe".formatted(req.getNombre().trim()), null);
        }
        
        Optional<RolEntity> optRol = RolMapper.toEntity(req);
        if(optRol.isEmpty()) {
            return ApiResponse.conflict("Rol %s no generado".formatted(req.getNombre().trim()), null);
        } 

        RolEntity creada = this.service.create(optRol.get());
        return ApiResponse.ok("Rol creada correctamente", RolMapper.toDto(creada));
    }

    // Eliminar un rol
    @DeleteMapping("/{rol_id}")
    public ResponseEntity<?> deleteEliminarRol(
        @PathVariable(name = "rol_id") Long rolId
    ) {
        try {
            ValidRequest.requireNonNull(rolId, "El campo rol_id es obligatoria");
            log.info("deleteEliminarRol recibido: {}", rolId);

            RolEntity rol = this.service.findById(rolId);
            if(Objects.isNull(rol)) {
                return ApiResponse.notFound("Rol con id(%d) no encontrada".formatted(rolId), null);
            }

            this.service.delete(rolId);
            return ApiResponse.ok("Rol con id(%d) eliminado correctamente".formatted(rolId), RolMapper.toDto(rol));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.conflict(e.getMessage(), null);
        } catch (BusinessException e) {
            return ApiResponse.conflict(e.getMessage(), null);
        }
    }
    
}
