package com.github.maximovj.rhhub_app.dto.autenticacion;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginOutDto {

    @JsonProperty(value = "acceso_token")
    private String accesoToken;

    @JsonProperty(value = "tipo_token")
    private String tipoToken;
    
    // Constructor personalizado adicional
    public LoginOutDto(String accesoToken) {
        this.accesoToken = accesoToken;
        this.tipoToken = "Bearer";
    }
    
}