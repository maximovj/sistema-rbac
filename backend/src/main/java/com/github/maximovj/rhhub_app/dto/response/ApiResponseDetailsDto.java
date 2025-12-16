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

    private int code; // 200, 201, 400, 404, etc.

    private String status; // success, error
    
    private Boolean success; // true, false

    private String message;

    private T context;

    private String messageError;

    private List<ApiErrorDto> errors;

    private Map<String, Object> metadata;

}
