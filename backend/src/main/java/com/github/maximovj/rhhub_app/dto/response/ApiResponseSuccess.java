package com.github.maximovj.rhhub_app.dto.response;

import org.springframework.http.HttpStatus;

public class ApiResponseSuccess<T> extends ApiResponseDetailsDto<T> {

    public ApiResponseSuccess(
        HttpStatus status,
        String message,
        T data
    ) {
        super(
            status.value(),
            "success",
            true,
            message,
            data,
            null,
            null,
            null);
    }
    
}
