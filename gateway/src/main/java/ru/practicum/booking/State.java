package ru.practicum.booking;

import ru.practicum.errorHandler.BadRequestException;

import java.util.stream.Stream;


public enum State {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static State fromString(String str) {
        try {
            return State.valueOf(str);
        } catch (Exception e) {
            throw new BadRequestException("Unknown state: " + str);
        }
    }
}

