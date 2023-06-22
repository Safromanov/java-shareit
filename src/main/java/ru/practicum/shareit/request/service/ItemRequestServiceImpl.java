package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemReq;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepo;
    private final UserRepository userRepo;

    @Override
    public ResponseItemReq createItemRequest(long userId, ItemRequestDto requestDto) {
        User owner = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        ItemRequest itemRequest = requestRepo.save(ItemRequestMapper.fromDto(requestDto, owner));
        return ItemRequestMapper.toResponseItemReq(itemRequest);
    }

    @Override
    public List<ResponseItemReq> getRequests(long userId, int from, int size) {
        userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return requestRepo.findByRequestorId(userId, page).stream()
                .map(ItemRequestMapper::toResponseItemReq)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseItemReq> getOtherUsersRequests(long userId, int from, int size) {
        userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return requestRepo.findByRequestorIdNot(userId, page)
                .map(ItemRequestMapper::toResponseItemReq)
                .getContent();
    }

    @Override
    public ResponseItemReq getItemRequest(long requesterId, long requestId) {
        userRepo.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        ItemRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new NotFoundException("You do not have permission to perform this operation"));
        if (requestId != request.getRequestor().getId())
            throw new NotFoundException("You do not have permission to perform this operation");
        return ItemRequestMapper.toResponseItemReq(request);
    }
}
