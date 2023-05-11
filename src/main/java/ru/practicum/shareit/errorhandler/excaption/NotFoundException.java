package ru.practicum.shareit.errorhandler.excaption;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) {
        super(msg);
    }
}
