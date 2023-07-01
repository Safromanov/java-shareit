package ru.practicum.errorHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.errorHandler.errorResponse.ValidationErrorResponse;
import ru.practicum.errorHandler.errorResponse.Violation;


import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    log.warn("{}: {} ({})", error.getField(), error.getDefaultMessage(), e.getClass().getSimpleName());
                    return new Violation(error.getField(), error.getDefaultMessage());
                })
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintValidationException(
            ConstraintViolationException e
    ) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> {
                            log.warn("{}: {} ({})", violation.getPropertyPath(), violation.getMessage(), e.getClass().getSimpleName());
                            return new Violation(violation.getPropertyPath().toString(),
                                    violation.getMessage());
                        }
                )
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }
}
