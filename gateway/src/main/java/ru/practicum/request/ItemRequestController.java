package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;


import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createRequest(@RequestHeader(USER_ID_HEADER) Long userId,
                                                @RequestBody @Valid ItemRequestDto request) {
        return itemRequestClient.createItemRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @RequestParam(defaultValue = "0") @Min(0) int from,
                                                 @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return itemRequestClient.getRequests(userId, from, size);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Object> getOtherUsersRequests(
            @RequestHeader(USER_ID_HEADER) Long requesterId,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return itemRequestClient.getOtherUsersRequests(requesterId, from, size);
    }

    @GetMapping(path = "/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(USER_ID_HEADER) Long requesterId,
                                              @PathVariable Long requestId) {
        return itemRequestClient.getItemRequest(requesterId, requestId);
    }
}
