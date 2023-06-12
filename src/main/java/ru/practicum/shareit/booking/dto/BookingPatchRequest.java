package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class BookingPatchRequest {

    @Positive
    private Long id;
    private Item item;
    private User booker;
    private Status status;
    private LocalDateTime start;
    private LocalDateTime end;
}
