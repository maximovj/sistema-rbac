package com.github.maximovj.rhhub_app.mapper;

import java.util.Optional;
import java.util.Set;

import com.github.maximovj.rhhub_app.dto.records.PermisoDTO;
import com.github.maximovj.rhhub_app.dto.request.PermisoRequest;
import com.github.maximovj.rhhub_app.entity.PermisoEntity;

public class PermisoMapper {

    public static PermisoDTO toDTO(PermisoEntity e) {
        return new PermisoDTO(e);
    }

    public static PermisoDTO toDto(PermisoEntity e) {
        return new PermisoDTO(e, Set.of());
    }

    public static Optional<PermisoEntity> toEntity(PermisoRequest req) {
        return Optional.of(new PermisoEntity(
            req.getPermiso_id(), 
            req.getAccion(), 
            req.getModulo(),
            false,
            null,
            null,
            null,
            Set.of()
        ));
    }

    public static void updateFromRequest(
        PermisoEntity permiso,
        PermisoRequest req
    ) {
        try {
            if(req.getAccion() != null && !req.getAccion().isBlank()) {
                permiso.setAccion(req.getAccion().trim());
            }
            
            if(req.getModulo() != null && !req.getModulo().isBlank()) {
                permiso.setModulo(req.getModulo().trim());
            }
        } catch (Exception e) {
            System.out.println("Hubo un error: %s".formatted(e.getMessage()));
        }

    }

    public static Optional<PermisoEntity> fromRequest(PermisoRequest req) {
        try {
            PermisoEntity permiso = new PermisoEntity();

            if(req.getAccion() != null && !req.getAccion().isBlank()) {
                permiso.setAccion(req.getAccion().trim());
            }

            if(req.getModulo() != null && !req.getModulo().isBlank()) {
                permiso.setModulo(req.getModulo().trim());
            }

            return Optional.of(permiso);
        } catch (Exception e) {
            System.out.println("Hubo un error: %s".formatted(e.getMessage()));
            return Optional.empty();
        }
    }
    
}
