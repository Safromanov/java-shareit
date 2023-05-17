package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto createItem(ItemDto itemDto, long userId) {

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        item = itemRepository.save(item);
        itemDto.setId(item.getId());
        log.info("Created item - {}", itemDto);
        return itemDto;
    }

    public ItemDto updateUser(ItemDto itemDto, long itemId, long userId) {
        Item item = itemRepository.findByIdAndOwnerId(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Item ID dont found"));
        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        itemRepository.save(item);
        log.info("Updated item - {}", itemDto);
        return ItemMapper.toItemDto(item);
    }

    public void deleteItemById(long id) {
        try {
            itemRepository.deleteById(id);
            log.info("Deleted item with id - {}", id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Item ID dont found");
        }
    }

    public List<ItemDto> getAllItemsByUserId(long id) {
        return itemRepository.getAllByOwnerId(id).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public ItemDto getById(long id) {
        return ItemMapper.toItemDto(itemRepository.getById(id));
    }

    public List<ItemDto> findByItemNameOrDesc(String str) {
        if (str.isBlank()) return new ArrayList<>();
        return itemRepository.findByNameOrDescription(str)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
