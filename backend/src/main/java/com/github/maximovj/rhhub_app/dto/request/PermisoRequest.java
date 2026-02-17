package com.github.maximovj.rhhub_app.dto.request;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermisoRequest {
    

    @PositiveOrZero(message = "El campo permiso_id debe ser un número postivo")
    @JsonProperty(value = "permiso_id")
    @Builder.Default
    private Long permiso_id = null;
    
    @NotBlank(message = "El campo accion es obligatoria")
    @Size(min = 3, max = 20, message = "El campo accion debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z_]+$", 
    message = "El campo accion solo puede contener letras y guion bajo")
    @JsonProperty(value = "accion")
    private String accion;

    @NotBlank(message = "El campo modulo es obligatoria")
    @Size(min = 3, max = 20, message = "El campo modulo debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z_]+$", 
    message = "El campo modulo solo puede contener letras y guion bajo")
    @JsonProperty(value = "modulo")
    private String modulo;

    @JsonProperty(value = "es_activo", required = false, defaultValue = "false")
    @Builder.Default
    private Boolean es_activo = false;

}
