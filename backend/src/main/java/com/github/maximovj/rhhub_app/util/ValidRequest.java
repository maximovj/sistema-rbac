package com.github.maximovj.rhhub_app.util;

import java.util.List;

import com.github.maximovj.rhhub_app.dto.response.ApiErrorDto;
import com.github.maximovj.rhhub_app.exception.BadRequestException;

public class ValidRequest {

    public static void requireNonNull(Object value) {
        if (value == null) {
            throw new BadRequestException("Hay un campo obligatorio");
        }
    }

    public static void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new BadRequestException(message);
        }
    }

    public static void requireNonNull(Object value, String message, String field) {
        if (value == null) {
            throw new BadRequestException(
                    message,
                    List.of(new ApiErrorDto(field, "El campo " + field + " es obligatorio"))
            );
        }
    }

}
