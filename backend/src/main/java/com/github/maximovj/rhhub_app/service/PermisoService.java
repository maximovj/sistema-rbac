package com.github.maximovj.rhhub_app.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.dto.request.PermisoRequest;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.PermisoEntity;
import com.github.maximovj.rhhub_app.mapper.PermisoMapper;
import com.github.maximovj.rhhub_app.repository.PermisoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PermisoService {

    private final PermisoRepository repo;

    public ResponseEntity<?> verTodosPermisos() {
        List<PermisoEntity> permisos = this.repo.findAll();
        return ApiResponse.ok("Lista de permisos", permisos);
    }

    public ResponseEntity<?> verUnPermiso(Long permisoId) {
        Objects.requireNonNull(permisoId, "El campo permiso_id es obligatoria");
        PermisoEntity permiso = this.repo.qBuscaPorIdCargaRelaciones(permisoId).orElse(null);

        if(permiso == null) {
            String messageError = "Entidad no encontrada permiso (%d)".formatted(permisoId);
            return ApiResponse.notFound(messageError, null);
        }

        return ApiResponse.ok("Entidad localizaad", PermisoMapper.toDTO(permiso));
    }

    public ResponseEntity<?> crearUnPermiso(PermisoRequest req) {
        try {
            Objects.requireNonNull(req.getPermisoAccion(), "El campo accion es obligatoria");
            Objects.requireNonNull(req.getPermisoModulo(), "El campo modulo es obligatoria");
    
            PermisoEntity permiso = this.repo.findByPermisoAccion(req.getPermisoAccion().trim()).orElse(null);
            if(permiso != null) {
                String messageError = "La entidad %s existe".formatted(req.getPermisoAccion());
                return ApiResponse.conflict(messageError, null);
            }

            PermisoEntity entidad = PermisoMapper.toEntity(req).orElse(null);
            if(entidad == null) {
                return ApiResponse.conflict("La entidad es obligatoria", null);
            }

            PermisoEntity guardado = this.repo.save(entidad);
            return ApiResponse.ok("Entidad creada correctamente", PermisoMapper.toDto(guardado));
        } catch (Exception e) {
            return ApiResponse.internalServerError(e.getMessage(), null);
        }
    }

    public ResponseEntity<?> actualizarUnPermiso(Long permisoId, PermisoRequest req) {
        try {
            Objects.requireNonNull(permisoId, "El campo permiso_id es obligatoria");
            Objects.requireNonNull(req.getPermisoAccion(), "El campo accion es obligatoria");
            Objects.requireNonNull(req.getPermisoModulo(), "El campo modulo es obligatoria");
    
            PermisoEntity entidad = this.repo.findById(permisoId).orElse(null);
            if(entidad == null) {
                return ApiResponse.badRequest("La entidad no fue localizada", null);
            }

            if(this.repo.existsByPermisoAccion(req.getPermisoAccion().trim()) 
                && !entidad.getPermisoAccion().trim().equals(req.getPermisoAccion().trim())) {
                String messageError = "La entidad con accion %s existe".formatted(req.getPermisoAccion());
                return ApiResponse.conflict(messageError, null);
            }

            if(req.getPermisoAccion() != null && !req.getPermisoAccion().isBlank()) {
                entidad.setPermisoAccion(req.getPermisoAccion().trim());
            }

            if(req.getPermisoAccion() != null && !req.getPermisoAccion().isBlank()) {
                entidad.setPermisoModulo(req.getPermisoModulo().trim());
            }

            PermisoEntity modificado = this.repo.save(entidad);
            return ApiResponse.ok("Entidad actualizada correctamente", PermisoMapper.toDto(modificado));
        } catch (Exception e) {
            return ApiResponse.internalServerError(e.getMessage(), null);
        }
    }

    public ResponseEntity<?> eliminarUnPermiso(Long permisoId) {
        try {
            Objects.requireNonNull(permisoId, "El campo permiso_id es obligatoria");
            PermisoEntity entidad = this.repo.findById(permisoId).orElse(null);

            if(entidad == null) {
                return ApiResponse.badRequest("La entidad no fue localizada", null);
            }

            this.repo.delete(entidad);
            return ApiResponse.ok("Entidad eliminada correctamente", PermisoMapper.toDto(entidad));
        } catch (Exception e) {
            return ApiResponse.internalServerError(e.getMessage(), null);
        }
    }
    
}
