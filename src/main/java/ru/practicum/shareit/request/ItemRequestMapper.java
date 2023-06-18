package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class ItemRequestMapper {

    public static ItemRequest fromDto(ItemRequestDto dto, User owner){
        return new ItemRequest( dto.getDescription(), owner, LocalDateTime.now());
    }

    public static ItemRequest toDto(ItemRequestDto dto, User owner){
        return new ItemRequest( dto.getDescription(), owner, LocalDateTime.now());
    }


}
