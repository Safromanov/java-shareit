package ru.practicum.shareit.request.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplMockTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;

    private final EasyRandom generator = new EasyRandom();

    @Test
    void createRequest() {
        ItemRequestDto requestDto = generator.nextObject(ItemRequestDto.class);
        User owner = generator.nextObject(User.class);
        ItemRequest itemRequest = ItemRequestMapper.fromDto(requestDto, owner);
        ResponseItemRequest responseItemRequest = ItemRequestMapper.toResponseItemReq(itemRequest);
        when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(owner));

        assertEquals(responseItemRequest.getCreated(),
                itemRequestService.createItemRequest(owner.getId(), requestDto).getCreated());
        assertEquals(responseItemRequest.getDescription(),
                itemRequestService.createItemRequest(owner.getId(), requestDto).getDescription());
    }
}
