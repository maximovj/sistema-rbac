package com.github.maximovj.rhhub_app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioRequest {

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", 
             message = "El usuario solo puede contener letras, números, puntos, guiones y guiones bajos")
    private String usuario;
    
    @NotBlank(message = "El campo correo es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    private String correo;
    
    //@NotBlank(message = "El campo contrasena es obligatoria")
    @Size(min = 8, max = 30, message = "La contraseña debe tener entre 8 y 30 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.-@#']+$", 
            message = "La contraseña solo puede contener letras, números, puntos, guiones, guiones bajos y simbolos (.-@#')")
    private String contrasena;
    
    //@NotBlank(message = "El campo confirmar_contrasena es obligatoria")
    @Size(min = 8, max = 30, message = "La contraseña debe tener entre 8 y 30 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.-@#']+$", 
            message = "La contraseña solo puede contener letras, números, puntos, guiones, guiones bajos y simbolos (.-@#')")
    @JsonProperty(value = "confirmar_contrasena")
    private String confirmar_contrasena;

    //@NotNull(message = "El campo es_activo es obligatorio y debe ser true o false")
    @JsonProperty(value = "es_activo", required = false, defaultValue = "false")
    @Builder.Default
    private Boolean es_activo = false;
    
}
