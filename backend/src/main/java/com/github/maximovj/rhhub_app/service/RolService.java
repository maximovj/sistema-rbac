package com.github.maximovj.rhhub_app.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.dto.request.RolRequest;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.RolEntity;
import com.github.maximovj.rhhub_app.mapper.RolMapper;
import com.github.maximovj.rhhub_app.repository.RolRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class RolService {

    private final RolRepository repo;

    public ResponseEntity<?> mostrarTodos() {
        List<RolEntity> roles = this.repo.findAll();
        return ApiResponse.ok("Lista de roles", roles);
    }

    public ResponseEntity<?> verDetalleRol(Long rolId){
        Objects.requireNonNull(rolId, "rol_id es obligatorio");
        RolEntity rol = this.repo.qBuscarPorIdCargaRelaciones(rolId).orElse(null);
        return ApiResponse.ok("Info de rol", RolMapper.toDTO(rol));
    }

    public ResponseEntity<?> crearRol(RolRequest req) {
        try {
            Objects.requireNonNull(req.getNombre(), "El campo nombre es obligatoria");
            Objects.requireNonNull(req.getDescripcion(), "El campo descripcion es obligatoria");

            if(this.repo.existsByNombre(req.getNombre().trim())) {
                String messageError = "La entidad con nombre %s existe".formatted(req.getNombre());
                return ApiResponse.conflict(messageError, null);
            }
            
            RolEntity entidad = RolMapper.toEntity(req).orElse(null);
            Objects.requireNonNull(entidad, "La entidad es obligatoria");

            RolEntity guardado  = this.repo.save(entidad);
            return ApiResponse.ok("Entidad creada correctamente", RolMapper.toDto(guardado));
        } catch (Exception e) {
            return ApiResponse.internalServerError(e.getMessage(), null);
        }
    }

    public ResponseEntity<?> actualizarRol(Long rolId, RolRequest req) {
        try {
            Objects.requireNonNull(rolId, "El campo rol_id es obligatoria");
            Objects.requireNonNull(req.getNombre(), "El campo nombre es obligatoria");
            Objects.requireNonNull(req.getDescripcion(), "El campo descripcion es obligatoria");

            RolEntity entidad = this.repo.findById(rolId).orElse(null);
            if(entidad == null) {
                return ApiResponse.badRequest("La entidad no fue localizada", null);
            }

            if(this.repo.existsByNombre(req.getNombre().trim()) && 
                !entidad.getNombre().trim().equals(req.getNombre().trim())) {
                String messageError = "La entidad con nombre %s existe".formatted(req.getNombre());
                return ApiResponse.conflict(messageError, null);
            }

            if(req.getEsActivo() != null) {
                entidad.setEsActivo(req.getEsActivo());
            }

            if(req.getEsAdministrador() != null) {
                entidad.setEsAdministrador(req.getEsAdministrador());
            }

            if(req.getNombre() != null && !req.getNombre().isBlank()) {
                entidad.setNombre(req.getNombre().trim());
            }

            if(req.getDescripcion() != null && !req.getDescripcion().isBlank()) {
                entidad.setDescripcion(req.getDescripcion().trim());
            }

            RolEntity actualizar  = this.repo.save(entidad);
            return ApiResponse.ok("Entidad actualizada correctamente", RolMapper.toDto(actualizar));
        } catch (Exception e) {
            System.out.println("Hubo un error: " + e.getMessage());
            return ApiResponse.internalServerError(e.getMessage(), null);
        }
    }

    public ResponseEntity<?> eliminarUnRol(Long rolId) {
        try {
            Objects.requireNonNull(rolId, "El campo rol_id es obligatoria");

            RolEntity entidad = this.repo.findById(rolId).orElse(null);
            if(entidad == null) {
                return ApiResponse.badRequest("La entidad no fue localizada", null);
            }

            this.repo.delete(entidad);
            return ApiResponse.ok("Entidad eliminada correctamente", RolMapper.toDto(entidad));
        } catch (Exception e) {
            return ApiResponse.internalServerError(e.getMessage(), null);
        }
        
        
    }
    
}
