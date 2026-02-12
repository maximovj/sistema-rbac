package com.github.maximovj.rhhub_app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolRequest {
    
    @PositiveOrZero(message = "El campo grupo_id debe ser un número postivo")
    @JsonProperty(value = "rol_id")
    @Builder.Default
    private Long rol_id = null;

    //@NotBlank(message = "El campo nombre es obligatoria")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z_]+$", 
    message = "El nombre solo puede contener letras y guiones bajos")
    private String nombre;

    //@NotBlank(message = "El campo descripcion es obligatoria")
    @Size(min = 3, max = 50, message = "El descripcion debe tener entre 3 y 50 caracteres")
    @Pattern(
        regexp = "^[\\p{L}0-9_.\\-@#' ]+$", // \\p{L} : incluye letras con tilde y ñ
        message = "La descripción solo puede contener letras, números y los símbolos (.-@#')"
    )
    private String descripcion;

}
