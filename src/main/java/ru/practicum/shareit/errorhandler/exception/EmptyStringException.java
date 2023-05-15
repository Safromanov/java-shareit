package ru.practicum.shareit.errorhandler.exception;

public class EmptyStringException extends RuntimeException {

    public EmptyStringException(String str) {
        super(str);
    }

}
