package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class BookingRequest {
    @NotNull
    @FutureOrPresent
    LocalDateTime start;

    @NotNull
    LocalDateTime end;
    @Positive
    long itemId;
}
