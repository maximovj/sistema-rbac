package com.github.maximovj.rhhub_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maximovj.rhhub_app.dto.request.FechaCreacionRequest;
import com.github.maximovj.rhhub_app.dto.request.PermisoRequest;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.PermisoEntity;
import com.github.maximovj.rhhub_app.exception.BusinessException;
import com.github.maximovj.rhhub_app.exception.ResourceNotFoundException;
import com.github.maximovj.rhhub_app.mapper.PermisoMapper;
import com.github.maximovj.rhhub_app.repository.specification.PermisoSpecBuilder;
import com.github.maximovj.rhhub_app.service.PermisoService;
import com.github.maximovj.rhhub_app.util.ValidRequest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/v1/permisos")
@AllArgsConstructor
@Slf4j
public class PermisoController {

    private final PermisoService service;

    @GetMapping("")
    public ResponseEntity<?> getPermisos(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        ApiResponse<PermisoEntity> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by("modulo").ascending());
        return response.okPage("Lista de permisos", this.service.findAll(pageable));
    }
    
    @GetMapping("/q/buscar")
    public ResponseEntity<?> getBuscarPermisos(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @ModelAttribute PermisoRequest req,
        @ModelAttribute(name = "fecha_creacion") FechaCreacionRequest fechaCreacionReq
    ) {
        log.info("getBuscarPermisos recibido: {} / {}", page, size);
        log.info("getBuscarPermisos recibido: {}", req.toString());
        log.info("getBuscarPermisos recibido: {}", fechaCreacionReq.toString());
        
        ApiResponse<PermisoEntity> response = new ApiResponse<>();
            Specification<PermisoEntity> specPermiso = new PermisoSpecBuilder()
                                                    .permisoId(req.getPermiso_id())
                                                    .accion(req.getAccion())
                                                    .modulo(req.getModulo())
                                                    .esActivo(req.getEs_activo())
                                                    .fechaCreacion(fechaCreacionReq)
                                                    .build();
        Pageable pageable = PageRequest.of(page, size, Sort.by("modulo").ascending());
        return response.okPage("Lista de permisos", this.service.findBySpecification(specPermiso, pageable));
    }
    
    
    @GetMapping("/{permiso_id}")
    public ResponseEntity<?> getVerDetallePermiso(
        @PathVariable(name = "permiso_id") Long permisoId
    ) {
        log.info("getVerDetallePermiso recibido: {}", permisoId);
        PermisoEntity permiso = this.service.findById(permisoId);
        if(Objects.isNull(permiso)) {
            return ApiResponse.notFound("Permiso no encontrada", null);
        }
        return ApiResponse.ok("Permiso localizada", PermisoMapper.toDto(permiso));
    }

    @PutMapping("/{permiso_id}")
    public ResponseEntity<?> putActualizarPermiso(
        @PathVariable(name = "permiso_id") Long permisoId,
        @RequestBody PermisoRequest req
    ) {
        ValidRequest.requireNonNull(req, "El cuerpo JSON es obligatoria");
        
        log.info("putActualizarPermiso recibido: {} / {}", permisoId, req.toString());
        PermisoEntity permiso = this.service.findById(permisoId);

        if(Objects.isNull(permiso)) {
            return ApiResponse.notFound("Permiso no encontrado", null);
        }

        PermisoMapper.updateFromRequest(permiso, req);
        PermisoEntity actualizada = this.service.update(permisoId, permiso);
        return ApiResponse.ok("Permiso actualizada correctamente", PermisoMapper.toDto(actualizada));
    }
    
    @PostMapping("")
    public ResponseEntity<?> postCrearPermiso(
        @RequestBody @Valid PermisoRequest req
    ) {
        ValidRequest.requireNonNull(req, "El cuerpo JSON es obligatoria");
        ValidRequest.requireNonNull(req.getAccion(),"El campo accion es obligatorio");
        ValidRequest.requireNonNull(req.getModulo(),"El campo modulo es obligatorio");

        log.info("postCrearPermiso recibido: {}", req.toString()); 
        boolean existe = this.service.estoExiste(req.getAccion().trim(), req.getModulo().trim());
        if(existe) {
            return ApiResponse.conflict("El permiso (%s) existe".formatted(req.getAccion()), null);
        }
        
        Optional<PermisoEntity> optPermiso = PermisoMapper.toEntity(req);
        if(optPermiso.isEmpty()) {
            return ApiResponse.conflict("La entidad no fue creada", null);
        }

        PermisoEntity creada = this.service.create(optPermiso.get());
        return ApiResponse.ok("Permiso creada correctamente", PermisoMapper.toDto(creada));
    }

    @DeleteMapping("/{permiso_id}")
    public ResponseEntity<?> deleteEliminarPermiso(
        @PathVariable(name = "permiso_id") Long permisoId
    ) {
        try {
            PermisoEntity permiso = this.service.findById(permisoId);
            if(Objects.isNull(permiso)) {
                return ApiResponse.notFound("Permiso con id(%d) no encontrada".formatted(permisoId), null);
            }

            this.service.delete(permisoId);
            return ApiResponse.ok("Permiso eliminado correctamente", null);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.conflict(e.getMessage(), null);
        } catch (BusinessException e) {
            return ApiResponse.conflict(e.getMessage(), null);
        }
    }

}
