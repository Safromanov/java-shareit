package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemReq;

import java.util.List;

public interface ItemRequestService {
    ResponseItemReq createItemRequest(long userId, ItemRequestDto requestDto);

    List<ResponseItemReq> getRequests(long userId, int from, int size);

    List<ResponseItemReq> getOtherUsersRequests(long userId, int from, int size);

    ResponseItemReq getItemRequest(long requesterId, long requestId);
}
