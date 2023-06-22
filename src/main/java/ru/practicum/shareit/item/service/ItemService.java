package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, long userId);

    ItemDto updateItem(ItemDto itemDto, long itemId, long userId);

    void deleteItemById(long id);

    List<ItemDto> getAllItemsByUserId(long idOwner, int from, int size);

    ItemDto getById(long itemId, long userId);

    List<ItemDto> findByItemNameOrDesc(String str);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
