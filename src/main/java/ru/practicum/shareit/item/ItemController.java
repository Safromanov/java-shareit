package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody ItemDto item, @RequestHeader(USER_ID_HEADER) Long userId){
        return itemService.createItem(item,  userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable long itemId){
        return itemService.getById(itemId);
    }

}
