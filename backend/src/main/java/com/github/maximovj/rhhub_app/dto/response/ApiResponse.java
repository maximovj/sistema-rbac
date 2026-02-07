package com.github.maximovj.rhhub_app.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse<T> {

    // !! Respuestas Génericas
    public static ResponseEntity<?> error(HttpStatus status, String messageError, List<ApiErrorDto> errors) {
        return ResponseEntity
            .status(status.value())
            .body(
                new ApiResponseError<Object>(status, messageError, errors)
            );
    }

    public static ResponseEntity<?> ok(HttpStatus status, String message, Object data) {
        return ResponseEntity
            .status(status.value())
            .body(
                new ApiResponseSuccess<Object>(status, message, data)
            );
    }

    // !! Respuestas Especificas // Exitosas
    public static ResponseEntity<?> ok(String message, Object data) {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.ok(new ApiResponseSuccess<Object>(status, message, data));
    }

    public ResponseEntity<?> okPage(String message, Page<T> data) {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.ok(new ApiResponseSuccessPage<>(status, message, data));
    }

    public static ResponseEntity<?> created(String message, Object data) {
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity.status(status.value()).body(new ApiResponseSuccess<Object>(status, message, data));
    }

    // !! Respuestas Especificas // Error
    public static ResponseEntity<?> unauthorized(String messageError, List<ApiErrorDto> errors) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status.value()).body(new ApiResponseError<Object>(status, messageError, errors));
    }

    public static ResponseEntity<?> forbidden(String messageError, List<ApiErrorDto> errors) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status.value()).body(new ApiResponseError<Object>(status, messageError, errors));
    }

    public static ResponseEntity<?> badRequest(String messageError, List<ApiErrorDto> errors) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.badRequest().body(new ApiResponseError<Object>(status, messageError, errors));
    }

    public static ResponseEntity<?> notFound(String messageError, List<ApiErrorDto> errors) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new ApiResponseError<Object>(status, messageError, errors));
    }

    public static ResponseEntity<?> noContent(String messageError, List<ApiErrorDto> errors) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        return ResponseEntity.status(status.value()).body(new ApiResponseError<Object>(status, messageError, errors));
    }

    public static ResponseEntity<?> conflict(String messageError, List<ApiErrorDto> errors) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status.value()).body(new ApiResponseError<Object>(status, messageError, errors));
    }

    public static ResponseEntity<?> internalServerError(String messageError, List<ApiErrorDto> errors) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.internalServerError().body(new ApiResponseError<Object>(status, messageError, errors));
    }

}
