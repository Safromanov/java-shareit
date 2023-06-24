package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingGetResponse;

public interface ItemResponse {
    long getId();

    String getName();

    String getDescription();

    Boolean isAvailable();

    BookingGetResponse getLastBooking();

    BookingGetResponse getNextBooking();

}
