package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@Value
public class BookingResponse {

    Long id;
    LocalDateTime start;
    LocalDateTime end;
    ItemDto item;
    UserDto booker;
    Status status;
}