package ru.practicum.booking;

import ru.practicum.errorHandler.BadRequestException;


public enum State {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static State fromString(String state) {
        switch (state.toUpperCase()) {
            case "ALL":
                return ALL;
            case "CURRENT":
                return CURRENT;
            case "PAST":
                return PAST;
            case "FUTURE":
                return FUTURE;
            case "WAITING":
                return WAITING;
            case "REJECTED":
                return REJECTED;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
    }
}
