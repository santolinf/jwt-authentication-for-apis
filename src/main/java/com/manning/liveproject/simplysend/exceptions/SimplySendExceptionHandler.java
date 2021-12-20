package com.manning.liveproject.simplysend.exceptions;

import com.manning.liveproject.simplysend.api.enums.ErrorCode;
import com.manning.liveproject.simplysend.api.dto.ErrorDto;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class SimplySendExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleUsernameAlreadyExists(UsernameAlreadyExistsException e) {
        ErrorDto.ErrorDtoBuilder errorResponse = createErrorResponse(ErrorCode.CONFLICT)
                .message(e.getMessage());

        return new ResponseEntity<>(errorResponse.build(), new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintValidationException(ConstraintViolationException e) {
        ErrorDto.ErrorDtoBuilder errorResponse = createErrorResponse(ErrorCode.BAD_REQUEST);

        e.getConstraintViolations().forEach(violation ->
            errorResponse.violation(violation.getPropertyPath().toString(), violation.getMessage())
        );

        return new ResponseEntity<>(errorResponse.build(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidIdentifierException.class)
    public ResponseEntity<ErrorDto> handleInvalidId(InvalidIdentifierException e) {
        ErrorDto.ErrorDtoBuilder errorResponse = createErrorResponse(ErrorCode.BAD_REQUEST)
                .message(e.getMessage());

        return new ResponseEntity<>(errorResponse.build(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ErrorDto.ErrorDtoBuilder errorResponse = createErrorResponse(ErrorCode.BAD_REQUEST)
                .message("Invalid value: " + e.getValue());

        return new ResponseEntity<>(errorResponse.build(), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ErrorDto.ErrorDtoBuilder errorResponse = createErrorResponse(ErrorCode.BAD_REQUEST)
                .message("Field values violations");

        e.getFieldErrors().forEach(fieldError -> {
            errorResponse.violation(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return new ResponseEntity<>(errorResponse.build(), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ErrorDto.ErrorDtoBuilder errorResponse = createErrorResponse(ErrorCode.BAD_REQUEST)
                .message(e.getMessage());

        return new ResponseEntity<>(errorResponse.build(), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ErrorDto.ErrorDtoBuilder errorResponse = createErrorResponse(ErrorCode.BAD_REQUEST)
                .message(e.getMessage());

        return new ResponseEntity<>(errorResponse.build(), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ErrorDto.ErrorDtoBuilder errorResponse = createErrorResponse(ErrorCode.BAD_REQUEST)
                .message(e.getMessage());

        return new ResponseEntity<>(errorResponse.build(), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ErrorDto.ErrorDtoBuilder errorResponse = createErrorResponse(ErrorCode.BAD_REQUEST)
                .message(e.getMessage());

        return new ResponseEntity<>(errorResponse.build(), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception e,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ErrorDto.ErrorDtoBuilder errorResponse = createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR)
                .message(e.getMessage());

        return new ResponseEntity<>(errorResponse.build(), headers, status);
    }

    private ErrorDto.ErrorDtoBuilder createErrorResponse(ErrorCode code) {
        return ErrorDto.builder()
                .timestamp(LocalDateTime.now())
                .code(code);
    }
}
