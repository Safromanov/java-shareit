package ru.practicum.shareit.errorHandler.exception;

public class AlreadyBookingException extends RuntimeException {
    public AlreadyBookingException(String msg) {
        super(msg);
    }
}
