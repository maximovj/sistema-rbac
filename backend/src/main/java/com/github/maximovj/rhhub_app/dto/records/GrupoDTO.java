package com.github.maximovj.rhhub_app.dto.records;

import java.util.Set;
import java.util.stream.Collectors;

import com.github.maximovj.rhhub_app.entity.PermisosEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioRolEntity;

public record GrupoDTO(
    Long usuario_grupo_id,
    String nombre,
    String descripcion,
    Boolean es_activo,
    UsuarioRolEntity rol,
    Set<PermisosEntity> permisos) {

        public GrupoDTO(UsuarioGruposEntity e) {
            this(e.getUsuarioGrupoId(), e.getNombre(), e.getDescripcion(), e.getEsActivo(), e.getRol(), e.getPermisos());
        }

        public GrupoDTO(UsuarioGruposEntity e, UsuarioRolEntity rol, Set<PermisosEntity> permisos) {
            this(e.getUsuarioGrupoId(), e.getNombre(), e.getDescripcion(), e.getEsActivo(), rol, permisos);
        }
    
}
