package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.errorhandler.excaption.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;


@Service
@RequiredArgsConstructor
@Validated
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto createItem( @Valid ItemDto itemDto, long userId){
        User owner;
        try {
            owner  = userRepository.getById(userId);
            owner.getName();
        } catch (EntityNotFoundException e){
            throw new NotFoundException("User dont found");
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        itemRepository.save(item);
        itemDto.setId(item.getId());
        return itemDto;
    }

    public ItemDto getById(long id){
        return ItemMapper.toItemDto(itemRepository.getById(id));
    }

}
