package ru.practicum.shareit.errorHandler.exception;


public class AlreadyExistException extends RuntimeException {

    public AlreadyExistException(String message) {
        super(message);
    }
}
