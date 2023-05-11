package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestBody Item item, @RequestHeader(USER_ID_HEADER) Long userId){
        return itemService.createItem(item,  userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable long itemId){
        return itemService.getById(itemId);
    }

}
