package com.github.maximovj.rhhub_app.exception;

import java.util.List;

import com.github.maximovj.rhhub_app.dto.response.ApiErrorDto;

public class NotFoundException extends BaseCustomException {

    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(String message, List<ApiErrorDto> errors) {
        super(message);
    }
    
}
