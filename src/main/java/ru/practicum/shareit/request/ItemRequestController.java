package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestServiceImpl;

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseItemRequest createRequest(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @RequestBody @Valid ItemRequestDto request) {
        return itemRequestServiceImpl.createItemRequest(userId, request);
    }

    @GetMapping
    public List<ResponseItemRequest> getRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @RequestParam(defaultValue = "0") @Min(0) int from,
                                                 @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return itemRequestServiceImpl.getRequests(userId, from, size);
    }

    @GetMapping(path = "/all")
    public List<ResponseItemRequest> getOtherUsersRequests(
            @RequestHeader(USER_ID_HEADER) Long requesterId,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return itemRequestServiceImpl.getOtherUsersRequests(requesterId, from, size);
    }

    @GetMapping(path = "/{requestId}")
    public ResponseItemRequest getItemRequest(@RequestHeader(USER_ID_HEADER) Long requesterId,
                                              @PathVariable Long requestId) {
        return itemRequestServiceImpl.getItemRequest(requesterId, requestId);
    }
}
