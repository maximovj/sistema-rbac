package com.github.maximovj.rhhub_app.dto.records;

import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioRolEntity;

public record UsuarioGruposDTO(
    Long usuario_grupo_id,
    String nombre,
    String descripcion,
    Boolean es_activo,
    UsuarioRolEntity rol) {

        public UsuarioGruposDTO(UsuarioGruposEntity e) {
            this(e.getUsuarioGrupoId(), e.getNombre(), e.getDescripcion(), e.getEsActivo(), e.getRol());
        }
    
}
