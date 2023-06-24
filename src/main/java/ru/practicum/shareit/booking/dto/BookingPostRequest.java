package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class BookingPostRequest {
    @NotNull
    @FutureOrPresent
    LocalDateTime start;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime end;
    @Positive
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    long itemId;
}
