package com.github.maximovj.rhhub_app.mapper;

import java.util.Optional;
import java.util.Set;

import com.github.maximovj.rhhub_app.dto.records.GrupoDTO;
import com.github.maximovj.rhhub_app.dto.request.GrupoRequest;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;

public class GrupoMapper {

    public static GrupoDTO toDTO(GrupoEntity e) {
        return new GrupoDTO(e);
    }

    public static GrupoDTO toDto(GrupoEntity e) {
        return new GrupoDTO(e, null, Set.of());
    }

    public static void updateFromRequest(GrupoEntity e, GrupoRequest req) {
        if(req.getNombre() != null && !req.getNombre().isBlank()) {
            e.setNombre(req.getNombre().trim());
        }

        if(req.getDescripcion() != null && !req.getDescripcion().isBlank()) {
            e.setDescripcion(req.getDescripcion().trim());
        }

        if(req.getEs_activo() != null) {
            e.setEsActivo(req.getEs_activo());
        }

    }

    public static Optional<GrupoEntity> fromRequest(GrupoRequest req) {
        try {
            GrupoEntity e = new GrupoEntity(
                null, 
                req.getNombre(), 
                req.getDescripcion(), 
                req.getEs_activo() == null ? false : req.getEs_activo(),
                null, 
                null);
            return Optional.of(e);
        } catch (Exception e) {
            System.out.println("Hubo un error: %s".formatted(e.getMessage()));
            return Optional.empty();
        }
    }
    
}
