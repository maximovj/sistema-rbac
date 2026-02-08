package com.github.maximovj.rhhub_app.mapper;

import com.github.maximovj.rhhub_app.dto.records.UsuarioDTO;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(UsuarioEntity e) 
    { 
        return new UsuarioDTO(e);
    }

    public static UsuarioDTO toDTOBasic(UsuarioEntity e) {
        return new UsuarioDTO(e.getUsuarioId(), 
            e.getUsuario(), 
            e.getCorreo(), 
            e.getEsActivo(), 
            e.getToken(), 
            null, 
            null);

    }
    
}
