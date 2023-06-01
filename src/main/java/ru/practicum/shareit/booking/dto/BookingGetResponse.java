package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface BookingGetResponse {
    Long getId();

    @JsonProperty("bookerId")
    Long getBooker();
}
