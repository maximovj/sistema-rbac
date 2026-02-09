package com.github.maximovj.rhhub_app.mapper;

import java.util.Set;

import com.github.maximovj.rhhub_app.dto.records.GrupoDTO;
import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;

public class GrupoMapper {

    public static GrupoDTO toDTO(UsuarioGruposEntity e) {
        return new GrupoDTO(e);
    }

    public static GrupoDTO toDto(UsuarioGruposEntity e) {
        return new GrupoDTO(e, null, Set.of());
    }
    
}
