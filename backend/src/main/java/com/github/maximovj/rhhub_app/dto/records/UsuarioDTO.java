package com.github.maximovj.rhhub_app.dto.records;

import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEstadoEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;

public record UsuarioDTO(
    Long usuario_id,
    String usuario,
    String correo,
    Boolean es_activo,
    String token,
    UsuarioGruposDTO grupo,
    UsuarioEstadoEntity estado) {

    public UsuarioDTO(UsuarioEntity e) {
        this(e.getUsuarioId(),
            e.getUsuario(),
            e.getCorreo(),
            e.getEsActivo(),
            e.getToken(),
            new UsuarioGruposDTO(e.getGrupo()),
            e.getEstado());
    }

}