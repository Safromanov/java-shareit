package ru.practicum.shareit.request;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemRequestMapperTest {
    private final EasyRandom generator = new EasyRandom();

    @Test
    void fromDto() {
        ItemRequestDto itemRequestDto = generator.nextObject(ItemRequestDto.class);
        User owner = generator.nextObject(User.class);
        ItemRequest itemRequest = ItemRequestMapper.fromDto(itemRequestDto, owner);
        assertThat(itemRequest.getDescription()).isEqualTo(itemRequestDto.getDescription());
        assertThat(itemRequest.getRequestor()).isEqualTo(owner);
        assertNotEquals(null, itemRequest.getCreated());
    }

    @Test
    void toResponseItemReq() {
        ItemRequest itemRequest = generator.nextObject(ItemRequest.class);
        ResponseItemRequest responseItemRequest = ItemRequestMapper.toResponseItemReq(itemRequest);
        assertThat(responseItemRequest.getDescription()).isEqualTo(itemRequest.getDescription());
        assertThat(responseItemRequest.getId()).isEqualTo(itemRequest.getId());
        assertThat(responseItemRequest.getCreated()).isEqualTo(itemRequest.getCreated());
        assertThat(responseItemRequest.getItems())
                .isEqualTo(itemRequest.getItems().stream()
                        .map(ItemMapper::toItemDto).collect(Collectors.toList()));
    }
}