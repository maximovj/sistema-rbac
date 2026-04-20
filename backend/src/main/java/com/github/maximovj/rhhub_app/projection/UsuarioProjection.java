package com.github.maximovj.rhhub_app.projection;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

public interface UsuarioProjection {
    
    @JsonProperty("usuario_id")
    Long getUsuarioId();
    
    @JsonProperty("usuario")
    String getUsuario();
    
    @JsonProperty("correo")
    String getCorreo();

    @JsonProperty("es_activo")
    Boolean getEsActivo();

    @JsonProperty("creado_en")
    public LocalDateTime getCreadoEn();

    @JsonProperty("actualizado_en")
    public LocalDateTime getActualizadoEn();
    
    @JsonProperty("eliminado_en")
    public LocalDateTime getEliminadoEn();
    
    static UsuarioProjection fromEntity(UsuarioEntity e) {
        return new UsuarioProjection() {
            public Long getUsuarioId() { return e.getUsuarioId(); }
            public String getUsuario() { return e.getUsuario(); }
            public String getCorreo() { return e.getCorreo(); }
            public Boolean getEsActivo() { return e.getEsActivo(); }
            public LocalDateTime getCreadoEn() { return e.getCreadoEn(); }
            public LocalDateTime getActualizadoEn() { return e.getActualizadoEn(); }
            public LocalDateTime getEliminadoEn() { return e.getEliminadoEn(); }
        };
    }
    
}
