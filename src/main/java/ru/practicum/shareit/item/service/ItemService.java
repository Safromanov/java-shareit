package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item createItem(@Valid Item item){
        return itemRepository.save(item);
    }

    public Item getById(long id){
        return itemRepository.getById(id);
    }

    public long countByLogin(String name) {
        return itemRepository.countByName(name);
    }
}
