package com.promo.management.system.promomanagement.web;

import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.SERVER_ERROR;

import java.util.Optional;

import com.promo.management.system.promomanagement.web.model.dto.response.ErrorDto;
import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeException;
import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({PromoSystemRuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDto> applicationException(PromoSystemRuntimeException ex) {
        log.atError().log(() -> String.format("PromoSystemRuntimeException: %s", ex));
        return applicationErrorResponseBuilder(ex.getCode(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({PromoSystemRuntimeValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDto> applicationValidationException(PromoSystemRuntimeValidationException ex) {
        log.atError().log(() -> String.format("PromoSystemRuntimeValidationException: %s", ex));
        return applicationErrorResponseBuilder(ex.getCode(), ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
                                                               WebRequest request) {
        String message = Optional.ofNullable(ex.getFieldError())
            .map(fieldError -> String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()))
            .orElseGet(() -> ex.getBindingResult().getGlobalError().getDefaultMessage());

        String code = ex.getStatusCode().toString();

        return new ResponseEntity<>(ErrorDto.builder().code(code).message(message).build(), status);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorDto> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        log.atError().log(() -> String.format("AuthorizationDeniedException: %s", ex));
        return applicationErrorResponseBuilder(HttpStatus.FORBIDDEN.toString(), ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.atError().log(() -> String.format("Unexpected error: %s", ex));
        return applicationErrorResponseBuilder(SERVER_ERROR.getCode(), SERVER_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<ErrorDto> applicationErrorResponseBuilder(String code, String message, HttpStatus status) {
        ErrorDto errorDto = ErrorDto.builder()
            .code(code)
            .message(message)
            .build();
        return new ResponseEntity<>(errorDto, status);
    }

}
