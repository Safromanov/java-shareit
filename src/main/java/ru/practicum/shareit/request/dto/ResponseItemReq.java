package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.item.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ResponseItemReq {

    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}


