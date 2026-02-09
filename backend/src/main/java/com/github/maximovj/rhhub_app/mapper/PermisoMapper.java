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
            req.getPermisoId(), 
            req.getPermisoAccion(), 
            req.getPermisoModulo(),
            Set.of()
        ));
    }
    
}
