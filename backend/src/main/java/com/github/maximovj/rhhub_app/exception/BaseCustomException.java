package com.github.maximovj.rhhub_app.exception;

import java.util.List;

import com.github.maximovj.rhhub_app.dto.response.ApiErrorDto;

public class BaseCustomException extends RuntimeException {

    protected final List<ApiErrorDto> errors;

    protected BaseCustomException(String message) {
        super(message);
        this.errors = List.of();
    }

    protected BaseCustomException(String message, List<ApiErrorDto> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ApiErrorDto> getErrors() {
        return errors;
    }
    
}
