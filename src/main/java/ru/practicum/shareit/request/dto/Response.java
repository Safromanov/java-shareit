package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class Response {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
    private List<ItemDto> items;
}
