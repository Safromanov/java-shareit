package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Create;
import ru.practicum.comment.CommentDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.groups.Default;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@RequestBody @Validated({Create.class, Default.class}) ItemDto item,
                                             @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.createItem(item, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable long itemId,
                                          @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByItemNameOrDesc(@RequestParam(value = "text", required = false) String str,
                                                       @RequestHeader(USER_ID_HEADER) long userId,
                                                       @RequestParam(defaultValue = "0") @Min(0) int from,
                                                       @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return itemService.findByItemNameOrDesc(userId, str, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUserId(@RequestHeader(USER_ID_HEADER) long userId,
                                                      @RequestParam(defaultValue = "0") @Min(0) int from,
                                                      @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody @Valid ItemDto itemDto,
                                             @PathVariable long itemId,
                                             @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
