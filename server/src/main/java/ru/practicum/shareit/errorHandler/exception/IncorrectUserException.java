package ru.practicum.shareit.errorHandler.exception;

public class IncorrectUserException extends RuntimeException {
    public IncorrectUserException(String msg) {
        super(msg);
    }
}
