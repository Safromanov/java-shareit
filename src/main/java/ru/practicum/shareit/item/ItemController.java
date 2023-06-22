package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.marker.Create;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.groups.Default;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemServiceImpl itemService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody @Validated({Create.class, Default.class}) ItemDto item,
                              @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.createItem(item, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable long itemId,
                           @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findByItemName(@RequestParam(value = "text", required = false) String str) {
        return itemService.findByItemNameOrDesc(str);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader(USER_ID_HEADER) long userId,
                                             @RequestParam(defaultValue = "0") @Min(0) int from,
                                             @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody @Valid ItemDto itemDto,
                              @PathVariable long itemId,
                              @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemId}")
    public void deleteUserById(@PathVariable long itemId,
                               @RequestHeader(USER_ID_HEADER) long userId) {
        itemService.deleteItemById(itemId);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                 @PathVariable("itemId") Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
