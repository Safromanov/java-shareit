package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.errorhandler.excaption.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Validated
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto createItem( @Valid ItemDto itemDto, long userId){
        User owner  = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User dont found"));

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);

        itemRepository.save(item);
        itemDto.setId(item.getId());

        return itemDto;
    }

    public ItemDto updateUser(@Valid ItemDto itemDto, long itemId, long userId) {
        Item item = itemRepository.findByIdAndOwner_Id(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Item ID dont found"));
        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    public void deleteItemById(long id) {
        try {
            itemRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Item ID dont found");
        }

    }

    public List<ItemDto> getAllItemsByUserId(long id){
        return itemRepository.getAllByOwner_Id(id).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public ItemDto getById(long id){
        return ItemMapper.toItemDto(itemRepository.getById(id));
    }

    public List<ItemDto> findByItemNameOrDesc(String itemName){
        return itemRepository.findByNameLikeIgnoreCase(itemName)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

}
