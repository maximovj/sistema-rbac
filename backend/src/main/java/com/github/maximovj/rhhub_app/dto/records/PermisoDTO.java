package com.github.maximovj.rhhub_app.dto.records;

import java.util.Set;
import java.util.stream.Collectors;

import com.github.maximovj.rhhub_app.entity.PermisosEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;

public record PermisoDTO(
    Long permisoId,
    String permisoAccion,
    String permisoModulo,
    Set<UsuarioGruposEntity> grupos) {

        public PermisoDTO(PermisosEntity e) {
            this(e.getPermisoId(), e.getPermisoAccion(), e.getPermisoModulo(), e.getGrupos());
        }

        public PermisoDTO(PermisosEntity e, Set<UsuarioGruposEntity> grupos) {
            this(e.getPermisoId(), e.getPermisoAccion(), e.getPermisoModulo(), grupos);
        }
    
}
