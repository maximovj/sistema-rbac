package com.github.maximovj.rhhub_app.dto.records;

import java.util.Set;
import java.util.stream.Collectors;

import com.github.maximovj.rhhub_app.entity.PermisosEntity;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.entity.RolEntity;

public record GrupoDTO(
    Long usuario_grupo_id,
    String nombre,
    String descripcion,
    Boolean es_activo,
    RolEntity rol,
    Set<PermisosEntity> permisos) {

        public GrupoDTO(GrupoEntity e) {
            this(e.getGrupoId(), e.getNombre(), e.getDescripcion(), e.getEsActivo(), e.getRol(), e.getPermisos());
        }

        public GrupoDTO(GrupoEntity e, RolEntity rol, Set<PermisosEntity> permisos) {
            this(e.getGrupoId(), e.getNombre(), e.getDescripcion(), e.getEsActivo(), rol, permisos);
        }
    
}
