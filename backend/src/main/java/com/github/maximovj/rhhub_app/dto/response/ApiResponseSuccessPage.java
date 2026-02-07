package com.github.maximovj.rhhub_app.dto.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

public class ApiResponseSuccessPage<T>
    extends ApiResponseSuccess<PageResponse<T>> {

    public ApiResponseSuccessPage(
        HttpStatus status,
        String message,
        Page<T> page
    ) {
        super(
            status,
            message,
            new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
            )
        );
    }
}