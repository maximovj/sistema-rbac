package com.github.maximovj.rhhub_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.maximovj.rhhub_app.dto.request.GrupoRequest;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.entity.PermisoEntity;
import com.github.maximovj.rhhub_app.exception.BusinessException;
import com.github.maximovj.rhhub_app.exception.ResourceNotFoundException;
import com.github.maximovj.rhhub_app.mapper.GrupoMapper;
import com.github.maximovj.rhhub_app.repository.specification.GrupoSpecBuilder;
import com.github.maximovj.rhhub_app.service.GrupoService;
import com.github.maximovj.rhhub_app.util.ValidRequest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        ApiResponse<GrupoEntity> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        return response.okPage("Lista de grupos", this.service.findAll(pageable)); 
    }

    // !! Buscar grupos
    @GetMapping("/q/busqueda")
    public ResponseEntity<?> getBusqueda(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @ModelAttribute GrupoEntity req
    ) {
        // Filtrar todos con paginación
        ApiResponse<GrupoEntity> response = new ApiResponse<>();
        Specification<GrupoEntity> spec = new GrupoSpecBuilder()
                                        .grupoId(req.getGrupoId())
                                        .nombre(req.getNombre())
                                        .descripcion(req.getDescripcion())
                                        .esActivo(req.getEsActivo())
                                        .build();
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        return response.okPage("OK", this.service.findBySpecification(spec, pageable));
    }

    // !! Ver un grupo por grupo_id
    @GetMapping("/{grupo_id}")
    public ResponseEntity<?> getVerUnGrupo(
        @PathVariable(name = "grupo_id") Long grupoId
    ) {
        ValidRequest.requireNonNull(grupoId, "El campo grupo_id es obligatoria");

        GrupoEntity grupo = this.service.findById(grupoId);
        if(Objects.isNull(grupo)) {
            return ApiResponse.notFound("Grupo no encontrada", null);
        }
        
        return ApiResponse.ok("OK", GrupoMapper.toDto(grupo));
    }

    // !! Actualizar un grupo
    @PutMapping("/{grupo_id}")
    public ResponseEntity<?> putActualizarGrupo(
        @PathVariable(value = "grupo_id") Long grupoId,
        @RequestBody GrupoRequest req
    ) {
        ValidRequest.requireNonNull(req, "El cuerpo JSON es obligatoria");
        ValidRequest.requireNonNull(req.getNombre(), "El campo nombre es obligatoria");
        log.info("putActualizarGrupo JSON recibido: {}", req.toString());

        GrupoEntity grupo = this.service.findById(grupoId);
        if(Objects.isNull(grupo)) {
            return ApiResponse.notFound("Grupo no encontrada", null);
        }

        boolean existe = this.service.esoExiste(req.getNombre());
        if(existe) {
            return ApiResponse.conflict("Grupo con nombre(%s) existe".formatted(req.getNombre()), null);
        }

        GrupoMapper.updateFromRequest(grupo, req);
        GrupoEntity actualizada = this.service.update(grupoId, grupo);
        return ApiResponse.ok("Grupo actualizado correctamente", GrupoMapper.toDto(actualizada));
    }

    // !! Crear un grupo
    @PostMapping("")
    public ResponseEntity<?> postCrearGrupo(
        @RequestBody() @Valid GrupoRequest req
    ) {
        ValidRequest.requireNonNull(req, "El cuerpo JSON es obligatoria");
        ValidRequest.requireNonNull(req.getNombre(), "El campo nombre es obligatoria");
        ValidRequest.requireNonNull(req.getDescripcion(), "El campo descripcion es obligatoria");
        log.info("putActualizarGrupo JSON recibido: {}", req.toString());

        boolean existe = this.service.esoExiste(req.getNombre());
        if(existe) {
            return ApiResponse.conflict("Grupo con nombre(%s) existe".formatted(req.getNombre()), null);
        }

        Optional<GrupoEntity> optGrupo =  GrupoMapper.fromRequest(req);
        if(optGrupo.isEmpty()) {
            return ApiResponse.conflict("Entidad no creada", null);
        }

        GrupoEntity creada = this.service.create(optGrupo.get());
        return ApiResponse.ok("Grupo creada correctamente", GrupoMapper.toDto(creada));
    }

    // !! Eliminar un grupo
    @DeleteMapping("/{grupo_id}")
    public ResponseEntity<?> deleteEliminarGrupo(
        @PathVariable(value = "grupo_id") Long grupoId
    ) {
        try {
            GrupoEntity permiso = this.service.findById(grupoId);
            if(Objects.isNull(permiso)) {
                return ApiResponse.notFound("Grupo con id(%d) no encontrada".formatted(grupoId), null);
            }

            this.service.delete(grupoId);
            return ApiResponse.ok("Grupo con id(%d) eliminado correctamente".formatted(grupoId), null);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.conflict(e.getMessage(), null);
        } catch (BusinessException e) {
            return ApiResponse.conflict(e.getMessage(), null);
        }
    }
    
}
