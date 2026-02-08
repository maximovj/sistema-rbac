package com.github.maximovj.rhhub_app.entity.embedded_id;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UsuarioPermisoEmbeddedId implements Serializable {

    @Column(name = "USUARIO_ID")
    private Long usuarioId;

    @Column(name = "PERMISO_ID")
    private Long permisoId;
    
}
