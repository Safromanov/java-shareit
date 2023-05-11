package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.Valid;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto createItem(@Valid Item item, long userId){
        User owner;

        try {
            owner  = userRepository.getById(userId);
            owner.getName();
        } catch (Exception e){
            throw new RuntimeException("User dont found");
        }


        item.setOwner(owner);
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto getById(long id){
        return ItemMapper.toItemDto(itemRepository.getById(id));
    }

}
