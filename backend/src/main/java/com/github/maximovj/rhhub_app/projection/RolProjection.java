package com.github.maximovj.rhhub_app.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.maximovj.rhhub_app.entity.RolEntity;

public interface RolProjection {

    @JsonProperty("nombre")
    String getNombre();
    
    @JsonProperty("descripcion")
    String getDescripcion();

    public static RolProjection fromEntity(RolEntity e) {
        return new RolProjection() {
            public String getNombre() { return e.getNombre(); }
            public String getDescripcion() { return e.getDescripcion(); }
        };
    }
    
}
