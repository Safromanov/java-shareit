package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public static ItemDto toItemGetDto(Item item, BookingGetResponse last, BookingGetResponse next) {
        var responseBuilder = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable());
        if (last != null) responseBuilder.lastBooking(last);
        if (next != null) responseBuilder.nextBooking(next);
        return responseBuilder.build();
    }

    public static Item toItem(ItemDto item, User owner) {
        return new Item(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                owner
        );
    }
}
