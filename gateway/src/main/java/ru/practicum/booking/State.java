package ru.practicum.booking;

public enum State {
    ALL("ALL"), CURRENT("CURRENT"), PAST("PAST"), FUTURE("FUTURE"), WAITING("WAITING"), REJECTED("REJECTED");

    State(String str) {
    }
}
