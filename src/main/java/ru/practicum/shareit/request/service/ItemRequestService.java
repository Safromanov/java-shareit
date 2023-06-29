package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequest;

import java.util.List;

public interface ItemRequestService {
    ResponseItemRequest createItemRequest(long userId, ItemRequestDto requestDto);

    List<ResponseItemRequest> getRequests(long userId, int from, int size);

    List<ResponseItemRequest> getOtherUsersRequests(long userId, int from, int size);

    ResponseItemRequest getItemRequest(long requesterId, long requestId);
}
