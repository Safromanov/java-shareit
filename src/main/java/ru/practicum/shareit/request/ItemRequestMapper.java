package ru.practicum.shareit.request;

import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequest fromDto(ItemRequestDto dto, User owner) {
        return new ItemRequest(dto.getDescription(), owner, LocalDateTime.now());
    }

    public static ResponseItemRequest toResponseItemReq(ItemRequest request) {
        List<ItemDto> itemsDto = request.getItems() == null
                ? new ArrayList<>()
                : request.getItems().stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        return new ResponseItemRequest(request.getId(),
                request.getDescription(),
                request.getCreated(),
                itemsDto);
    }
}
