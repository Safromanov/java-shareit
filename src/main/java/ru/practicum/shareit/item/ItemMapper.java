package ru.practicum.shareit.item;

import ru.practicum.shareit.user.User;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable()
        );
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
