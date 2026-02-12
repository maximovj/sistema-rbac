package com.github.maximovj.rhhub_app.controller;

import com.github.maximovj.rhhub_app.dto.request.UsuarioRequest;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.RolEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.exception.BusinessException;
import com.github.maximovj.rhhub_app.exception.ResourceNotFoundException;
import com.github.maximovj.rhhub_app.mapper.RolMapper;
import com.github.maximovj.rhhub_app.mapper.UsuarioMapper;
import com.github.maximovj.rhhub_app.projection.UsuarioProjection;
import com.github.maximovj.rhhub_app.repository.specification.UsuarioSpecBuilder;
import com.github.maximovj.rhhub_app.service.UsuarioService;
import com.github.maximovj.rhhub_app.util.ValidRequest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/v1/usuarios")
@AllArgsConstructor
@Slf4j
public class UsuarioController {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<?> getListarUsuarios(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        ApiResponse<UsuarioProjection> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by("usuario").ascending());
        return response.okPage("Filtro de usuarios", this.usuarioService.listarUsuarios(pageable));
    }

    @GetMapping("/q/buscar")
    public ResponseEntity<?> getBuscarUsuarios(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @ModelAttribute UsuarioRequest req
    ) {
        ApiResponse<UsuarioProjection> response = new ApiResponse<>();
        Specification<UsuarioEntity> spec = new UsuarioSpecBuilder()
                                            .usuarioId(req.getUsuario_id())
                                            .usuario(req.getUsuario())
                                            .correo(req.getCorreo())
                                            .build();
        Pageable pageable = PageRequest.of(page, size, Sort.by("usuario").ascending());
        return response.okPage("Filtro de usuarios", this.usuarioService.buscarUsuarios(spec, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putActualizarUsuario(
        @PathVariable Long id,
        @RequestBody UsuarioRequest req
    ) {
        ValidRequest.requireNonNull(id, "El campo id es obligatoria");
        ValidRequest.requireNonNull(req, "El cuerpo (JSON) de la petición es obligatoria");
        
        UsuarioEntity usuario = this.usuarioService.findById(id);
        if(Objects.isNull(usuario)) {
            return ApiResponse.conflict("Usuario no encontrada", null);
        }

        // Actualizamos la entidad usando el mapper
        UsuarioMapper.updateFromRequest(usuario, req, passwordEncoder);
        
        UsuarioEntity usuarioActualizada = this.usuarioService.update(id, usuario);
        return ApiResponse.ok("Usuario actualizada correctamente", UsuarioMapper.toDTOBasic(usuarioActualizada));
    }

    @PostMapping
    public ResponseEntity<?> postCrearUsuario(
        @RequestBody @Valid UsuarioRequest req
    ) {
        ValidRequest.requireNonNull(req, "El cuerpo (JSON) de la petición es obligatoria");
        
        boolean existeUsuario = this.usuarioService.existsByUsuarioOrCorreo(req.getUsuario().trim(), req.getCorreo().trim());
        if(existeUsuario){
            return ApiResponse.conflict("Usuario y/o correo no disponible", null);
        }
        
        // Mapear los datos
        Optional<UsuarioEntity> mapeoReq = UsuarioMapper.fromRequest(req, passwordEncoder);
        if(mapeoReq.isEmpty()) {
            return ApiResponse.conflict("El mapeo del usuario es incorrecto", null);
        }
        
        UsuarioEntity nuevoUsuario = mapeoReq.get();
        UsuarioEntity creada = this.usuarioService.create(nuevoUsuario);
        return ApiResponse.ok("Usuario creada correctamente", UsuarioMapper.toDTOBasic(creada));
    }

    @GetMapping("/{usuario_id}")
    public ResponseEntity<?> getVerUnUsuario(
        @PathVariable(required = true, name = "usuario_id") Long usuarioId
    ) {
        ValidRequest.requireNonNull(usuarioId, "El campo id es obligatoria");
        
        UsuarioEntity usuario = this.usuarioService.findById(usuarioId);
        if(usuario == null) {
            return ApiResponse.notFound("Usuario no encontrado", null);
        }
        return ApiResponse.ok("Info del usuario localizado correctamente", UsuarioMapper.toDTOBasic(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEliminarUnUsuario(@PathVariable Long id) {
        try {
           ValidRequest.requireNonNull(id, "El campo usuario_id es obligatoria");
            log.info("deleteEliminarUnUsuario recibido: {}", id);

            UsuarioEntity usuario = this.usuarioService.findById(id);
            if(Objects.isNull(usuario)) {
                return ApiResponse.notFound("Usuario con id(%d) no encontrada".formatted(id), null);
            }

            this.usuarioService.delete(id);
            return ApiResponse.ok("Usuario con id(%d) eliminado correctamente".formatted(id), UsuarioMapper.toDTOBasic(usuario));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.conflict(e.getMessage(), null);
        } catch (BusinessException e) {
            return ApiResponse.conflict(e.getMessage(), null);
        }
    }
 
}
