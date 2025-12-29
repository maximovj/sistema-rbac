package com.github.maximovj.rhhub_app.dto.response;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDetailsDto<T> {

    private int codigo; // 200, 201, 400, 404, etc.

    private String razon;

    private String estado; // success, error
    
    private Boolean exitosa; // true, false

    private String mensaje;

    private T contenido;

    private String error;

    private List<ApiErrorDto> errores;

    private Map<String, Object> metadata;

}
