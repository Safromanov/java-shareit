package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .comments(item.getComments().stream()
                        .map(CommentMapper::commentToDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static ItemDto toItemGetDto(Item item, BookingGetResponse last, BookingGetResponse next) {
        var responseBuilder = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .comments(item.getComments().stream()
                        .map(CommentMapper::commentToDto)
                        .collect(Collectors.toList()));
        if (last != null) responseBuilder.lastBooking(last);
        if (next != null) responseBuilder.nextBooking(next);
        return responseBuilder.build();
    }

    public static Item toItem(ItemDto item, User owner) {
        return new Item(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                owner, new ArrayList<>()
        );
    }
}
