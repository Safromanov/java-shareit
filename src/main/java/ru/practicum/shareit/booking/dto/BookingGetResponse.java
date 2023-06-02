package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;


public interface BookingGetResponse {
    Long getId();

    @Value("#{target.item.id}")
    @JsonInclude
    Long getItemId();

    @Value("#{target.booker.id}")
    @JsonProperty("bookerId")
    Long getBookerId();
}
