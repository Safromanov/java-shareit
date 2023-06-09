package ru.practicum.shareit.request.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserRepository userRepository;

    private final EasyRandom generator = new EasyRandom();
    private User requestor;

    private ResponseItemRequest responseItemRequest;

    @BeforeEach
    public void setUp() {
        requestor = generator.nextObject(User.class);
        requestor.setEmail("email@Test.ru");
        requestor = userRepository.save(requestor);
        responseItemRequest =
                itemRequestService.createItemRequest(requestor.getId(), generator.nextObject(ItemRequestDto.class));
    }

    @Test
    void createItemRequest() {
        assertNotNull(responseItemRequest.getId());
        assertNotNull(responseItemRequest.getCreated());
    }

    @Test
    void getRequests() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getRequests(0, 0, 10));
        List<ResponseItemRequest> responseItemRequestList = List.of(
                itemRequestService.createItemRequest(requestor.getId(), generator.nextObject(ItemRequestDto.class)),
                itemRequestService.createItemRequest(requestor.getId(), generator.nextObject(ItemRequestDto.class)));
        List<ResponseItemRequest> responseItemRequestListActual = itemRequestService.getRequests(requestor.getId(), 0, 10);
        assertThat(responseItemRequestListActual.size()).isEqualTo(responseItemRequestList.size() + 1);
        assertThat(responseItemRequestListActual.get(0).getId()).isEqualTo(responseItemRequest.getId());
    }

    @Test
    void getOtherUsersRequests() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getOtherUsersRequests(0, 0, 10));
        User user2 = generator.nextObject(User.class);
        user2.setEmail("email2@Test.ru");
        user2 = userRepository.save(user2);

        User user3 = generator.nextObject(User.class);
        user3.setEmail("email3@Test.ru");
        user3 = userRepository.save(user3);

        List<ResponseItemRequest> responseItemRequestList = List.of(
                itemRequestService.createItemRequest(user2.getId(), generator.nextObject(ItemRequestDto.class)),
                itemRequestService.createItemRequest(user2.getId(), generator.nextObject(ItemRequestDto.class)),
                itemRequestService.createItemRequest(user3.getId(), generator.nextObject(ItemRequestDto.class))
        );

        List<ResponseItemRequest> responseItemRequestListActual = itemRequestService.getOtherUsersRequests(requestor.getId(), 0, 10);

        assertThat(responseItemRequestListActual.size()).isEqualTo(responseItemRequestList.size());
        assertThat(responseItemRequestListActual.get(0).getId()).isEqualTo(responseItemRequestList.get(0).getId());
    }

    @Test
    void getItemRequest() {
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getItemRequest(0, responseItemRequest.getId()));

        assertThrows(NotFoundException.class, () ->
                itemRequestService.getItemRequest(requestor.getId(), responseItemRequest.getId() + 1));

        ResponseItemRequest responseItemRequestActual =
                itemRequestService.getItemRequest(requestor.getId(), responseItemRequest.getId());
        assertThat(responseItemRequestActual.getId()).isEqualTo(responseItemRequest.getId());
        assertNotNull(responseItemRequest.getCreated());
    }
}