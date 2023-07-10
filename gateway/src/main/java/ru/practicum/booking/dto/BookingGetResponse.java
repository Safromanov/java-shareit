package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookingGetResponse {
    private final Long id;
    private final Long itemId;
    private final Long bookerId;
}
