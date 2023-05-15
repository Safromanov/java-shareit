package ru.practicum.shareit.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.errorhandler.exception.AlreadyExistException;
import ru.practicum.shareit.errorhandler.exception.EmptyStringException;
import ru.practicum.shareit.errorhandler.exception.NotFoundException;
import ru.practicum.shareit.errorhandler.violation.ValidationErrorResponse;
import ru.practicum.shareit.errorhandler.violation.Violation;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseBody
    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public List<String> handleValidationError(final AlreadyExistException e) {
        return List.of(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<String> notFoundException(NotFoundException e) {
        return List.of(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(EmptyStringException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> emptyStringException(EmptyStringException e) {
        return List.of(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e
    ) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

}
