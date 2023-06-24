package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookingGetResponse {

//    Long getId();
//
//    @Value("#{target.item.id}")
//    @JsonInclude
//    Long getItemId();
//
//    @Value("#{target.booker.id}")
//    @JsonProperty("bookerId")
//    Long getBookerId();

    private final Long id;
    private final Long itemId;
    private final Long bookerId;
}
