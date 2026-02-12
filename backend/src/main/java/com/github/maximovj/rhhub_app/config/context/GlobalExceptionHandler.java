package com.github.maximovj.rhhub_app.config.context;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.github.maximovj.rhhub_app.dto.response.ApiErrorDto;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.dto.response.ApiResponseError;
import com.github.maximovj.rhhub_app.exception.BadRequestException;
import com.github.maximovj.rhhub_app.exception.BaseCustomException;
import com.github.maximovj.rhhub_app.exception.NotFoundException;
import com.github.maximovj.rhhub_app.exception.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // !! Error de excepción personalizado
    @ExceptionHandler(BaseCustomException.class)
    public ResponseEntity<?> handleBaseException(BaseCustomException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; 
        if (ex instanceof BadRequestException) status = HttpStatus.BAD_REQUEST;
        if (ex instanceof NotFoundException) status = HttpStatus.NOT_FOUND;
        if (ex instanceof ResourceNotFoundException) status = HttpStatus.NOT_FOUND;

        return ResponseEntity.status(status)
                .body(new ApiResponseError<>(status, ex.getMessage(), ex.getErrors()));
    }

    // Exception (catch-all FINAL)
    // Siempre deja uno genérico al final:
    // Evita stacktraces filtrados al cliente.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        List<ApiErrorDto> errores = new ArrayList<>();
        errores.add(new ApiErrorDto("cause", ex.getCause()));
        errores.add(new ApiErrorDto("message", ex.getMessage()));

        return ApiResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Hubo un error interno en el servidor",
            errores
        );
    }

    // !! Seguridad / permisos (Spring Security) 
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied() {
        return ApiResponse.error(
            HttpStatus.FORBIDDEN,
            "No tienes permisos para realizar esta acción",
            null
        );
    }

    // !! Errores de BD (UNIQUE, FK, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(
            DataIntegrityViolationException ex) {
        List<ApiErrorDto> errores = new ArrayList<>();
        errores.add(new ApiErrorDto("cause", ex.getCause().getMessage()));
        errores.add(new ApiErrorDto("specific_cause", ex.getMostSpecificCause().getMessage()));
        errores.add(new ApiErrorDto("message", ex.getMessage()));

        return ApiResponse.badRequest(
            "Violación de integridad de datos",
            errores
        );
    }

    // !! Endpoint no existe (404 real)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFound(NoHandlerFoundException ex) {
        List<ApiErrorDto> errores = new ArrayList<>();
        errores.add(new ApiErrorDto("cause", ex.getCause()));
        errores.add(new ApiErrorDto("message", ex.getMessage()));

        return ApiResponse.notFound(
            "Recurso no encontrado",
            errores
        );
    }

    // !! Método HTTP incorrecto (POST en vez de PUT, etc.)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {
        List<ApiErrorDto> errores = new ArrayList<>();
        errores.add(new ApiErrorDto("cause", ex.getCause().getMessage()));
        errores.add(new ApiErrorDto("message", ex.getMessage()));

        return ApiResponse.error(
            HttpStatus.METHOD_NOT_ALLOWED,
            "Método HTTP no permitido",
            errores
        );
    }

    // !! Tipo incorrecto en path o query param
    // !! ej. /usuarios/abc -> cuando esperas Long
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        List<ApiErrorDto> errores = new ArrayList<>();
        errores.add(new ApiErrorDto("cause", ex.getCause().getMessage()));
        errores.add(new ApiErrorDto("specific_cause", ex.getMostSpecificCause().getMessage()));
        errores.add(new ApiErrorDto("message", ex.getMessage()));
        errores.add(new ApiErrorDto(ex.getName(), "Valor inválido: " + ex.getValue()));

        return ApiResponse.badRequest(
            "Tipo de dato incorrecto",
            errores
        );
    }

    // !! Falta un @RequestParam obligatorio
    // !! Request sin ?nombre= 💥
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingRequestParam(
            MissingServletRequestParameterException ex) {
        List<ApiErrorDto> errores = new ArrayList<>();
        errores.add(new ApiErrorDto("cause", ex.getCause().getMessage()));
        errores.add(new ApiErrorDto("message", ex.getMessage()));
        errores.add(new ApiErrorDto(ex.getParameterName(), "El parámetro es obligatorio"));

        return ApiResponse.badRequest(
            "Falta un parámetro requerido",
            errores
        );
    }

    // !! Validaciones en @PathVariable y @RequestParam
    // Si llega id = 0 → explota aquí.
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(
            ConstraintViolationException ex) {

        List<ApiErrorDto> errors = new ArrayList<>();

        ex.getConstraintViolations().forEach(v -> {
            errors.add(new ApiErrorDto(
                v.getPropertyPath().toString(),
                v.getMessage()
            ));
        });

        return ApiResponse.badRequest("Parámetros inválidos", errors);
    }

    // !! JSON mal formado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidJson(
            HttpMessageNotReadableException ex) {
        List<ApiErrorDto> errores = new ArrayList<>();
        errores.add(new ApiErrorDto("cause", ex.getCause().getMessage()));
        errores.add(new ApiErrorDto("specific_cause", ex.getMostSpecificCause().getMessage()));
        errores.add(new ApiErrorDto("message", ex.getMessage()));

        return ApiResponse.badRequest("El cuerpo de la petición tiene un JSON inválido", errores);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        List<ApiErrorDto> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.add(new ApiErrorDto(error.getField(), error.getDefaultMessage()));
        });

        return ApiResponse.badRequest("Error en la validación", errors);
    }

}
