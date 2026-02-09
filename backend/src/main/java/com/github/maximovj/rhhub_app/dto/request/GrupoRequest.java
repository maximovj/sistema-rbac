package com.github.maximovj.rhhub_app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GrupoRequest {

    @PositiveOrZero(message = "El campo grupo_id debe ser un número postivo")
    @JsonProperty(value = "grupo_id")
    @Builder.Default
    private Long grupoId = null;

    //@NotBlank(message = "El campo nombre es obligatoria")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z]+$", 
    message = "El nombre solo puede contener letras")
    private String nombre;
    
    //@NotBlank(message = "El campo descripcion es obligatoria")
    @Size(min = 3, max = 50, message = "El descripcion debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.-@#' ]+$", 
            message = "La descripcion solo puede contener letras, números, puntos, guiones, guiones bajos y simbolos (.-@#')")
    private String descripcion;
    
    @JsonProperty(value = "es_activo", defaultValue = "true")
    @Builder.Default
    private Boolean esActivo = true;
    
}
