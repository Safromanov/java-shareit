package ru.practicum.shareit.errorhandler.excaption;



public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
    }
}
