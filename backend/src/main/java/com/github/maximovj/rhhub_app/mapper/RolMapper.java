package com.github.maximovj.rhhub_app.mapper;

import java.util.List;
import java.util.Optional;

import com.github.maximovj.rhhub_app.dto.records.RolDTO;
import com.github.maximovj.rhhub_app.dto.request.RolRequest;
import com.github.maximovj.rhhub_app.entity.RolEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RolMapper {

    public static RolDTO toDTO(RolEntity e) {
        return new RolDTO(e);
    }
    
    public static RolDTO toDto(RolEntity e) {
        return new RolDTO(e, List.of());
    }

    public static Optional<RolEntity> toEntity(RolRequest req) {
        try {
            RolEntity entidad = new RolEntity();
            if(req.getNombre() != null && !req.getNombre().isBlank()) {
                entidad.setNombre(req.getNombre().trim());
            }

            if(req.getDescripcion() != null && !req.getDescripcion().isBlank()) {
                entidad.setDescripcion(req.getDescripcion().trim());
            }
            return Optional.of(entidad);
        } catch (Exception e) {
            log.info("Hubo un error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static void updateFromRequest(RolEntity entidad, RolRequest req) {
        try {

            if(req.getNombre() != null && !req.getNombre().isBlank()) {
                entidad.setNombre(req.getNombre().trim());
            }

            if(req.getDescripcion() != null && !req.getDescripcion().isBlank()) {
                entidad.setDescripcion(req.getDescripcion().trim());
            }

        } catch (Exception e) {
            log.info("Hubo un error: {}", e.getMessage());
        }
    }

}
