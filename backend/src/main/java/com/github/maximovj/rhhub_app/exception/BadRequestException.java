package com.github.maximovj.rhhub_app.exception;

import java.util.List;

import com.github.maximovj.rhhub_app.dto.response.ApiErrorDto;

public class BadRequestException extends BaseCustomException {

    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String message, List<ApiErrorDto> errors) {
        super(message);
    }

}
