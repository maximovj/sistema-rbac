package com.github.maximovj.rhhub_app.projection;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface UsuarioProjection {
    
    @JsonProperty("usuario_id")
    Long getUsuarioId();
    
    @JsonProperty("usuario")
    String getUsuario();
    
    @JsonProperty("correo")
    String getCorreo();
    
    @JsonProperty("es_activo")
    Boolean getEsActivo();
    
}
