package com.github.maximovj.rhhub_app.dto.request;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioRequest {

    //@NonNull
    //@NotBlank(message = "El usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", 
             message = "El usuario solo puede contener letras, números, puntos, guiones y guiones bajos")
    private String usuario;
    
    //@NonNull
    //@NotBlank(message = "El campo correo es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    private String correo;
    
    //@NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.-@#']+$", 
            message = "La contraseña solo puede contener letras, números, puntos, guiones, guiones bajos y simbolos (.-@#')")
    private String contrasena;
    
    //@NonNull
    //@NotBlank(message = "El campo confirmar_contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.-@#']+$", 
            message = "La contraseña solo puede contener letras, números, puntos, guiones, guiones bajos y simbolos (.-@#')")
    private String confirmar_contrasena;
    
}
