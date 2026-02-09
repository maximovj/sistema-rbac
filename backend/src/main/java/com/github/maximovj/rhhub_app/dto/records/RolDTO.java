package com.github.maximovj.rhhub_app.dto.records;

import java.util.List;

import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.entity.RolEntity;

public record RolDTO(
    Long rol_id,
    String nombre,
    String descripcion,
    Boolean es_administrador,
    Boolean es_activo,
    List<GrupoEntity> grupos) {

        public RolDTO(RolEntity e) {
            this(e.getRolId(), e.getNombre(), e.getDescripcion(), e.getEsAdministrador(), e.getEsActivo(), e.getGrupos());
        }

        public RolDTO(RolEntity e, List<GrupoEntity> grupos) {
            this(e.getRolId(), e.getNombre(), e.getDescripcion(), e.getEsAdministrador(), e.getEsActivo(), grupos);
        }
    
}
