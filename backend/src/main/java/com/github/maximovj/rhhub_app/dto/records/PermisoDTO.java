package com.github.maximovj.rhhub_app.dto.records;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.maximovj.rhhub_app.entity.PermisoEntity;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;

public record PermisoDTO(
    Long permisoId,
    String permisoAccion,
    String permisoModulo,
    Boolean esActivo,
    LocalDateTime creadoEn,
    LocalDateTime actualizadoEn,
    LocalDateTime eliminadoEn,
    Set<GrupoEntity> grupos) {

        public PermisoDTO(PermisoEntity e) {
            this(e.getPermisoId(), e.getAccion(), e.getModulo(), e.getEsActivo(), e.getCreadoEn(), e.getActualizadoEn(), e.getEliminadoEn(), e.getGrupos());
        }

        public PermisoDTO(PermisoEntity e, Set<GrupoEntity> grupos) {
            this(e.getPermisoId(), e.getAccion(), e.getModulo(), e.getEsActivo(), e.getCreadoEn(), e.getActualizadoEn(), e.getEliminadoEn(), grupos);
        }
    
}
