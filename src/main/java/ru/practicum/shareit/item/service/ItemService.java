package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import javax.validation.Valid;

public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(@Valid Item item){
        return itemRepository.save(item);
    }

    public Item getById(long id){
        return itemRepository.getById(id);
    }
}
