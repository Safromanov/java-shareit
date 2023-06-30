package ru.practicum.shareit.item;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ItemMapperTest {

    private final EasyRandom generator = new EasyRandom();

    @Test
    void toItemDto() {
        Item item = generator.nextObject(Item.class);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        assertThat(itemDto.getId()).isEqualTo(item.getId());
        assertThat(itemDto.getName()).isEqualTo(item.getName());
        assertThat(itemDto.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemDto.getRequestId()).isEqualTo(item.getItemRequest().getId());
        assertThat(itemDto.getAvailable()).isEqualTo(item.isAvailable());
        assertThat(itemDto.getComments())
                .isEqualTo(item.getComments().stream().map(CommentMapper::commentToDto).collect(Collectors.toList()));
    }

    @Test
    void toItemGetDto() {
        Item item = generator.nextObject(Item.class);
        BookingGetResponse last = generator.nextObject(BookingGetResponse.class);
        BookingGetResponse next = generator.nextObject(BookingGetResponse.class);
        ItemDto itemDto = ItemMapper.toItemGetDto(item, last, next);
        assertThat(itemDto.getId()).isEqualTo(item.getId());
        assertThat(itemDto.getName()).isEqualTo(item.getName());
        assertThat(itemDto.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemDto.getRequestId()).isEqualTo(item.getItemRequest().getId());
        assertThat(itemDto.getAvailable()).isEqualTo(item.isAvailable());
        assertThat(itemDto.getComments())
                .isEqualTo(item.getComments().stream().map(CommentMapper::commentToDto).collect(Collectors.toList()));
        assertThat(itemDto.getLastBooking()).isEqualTo(last);
        assertThat(itemDto.getNextBooking()).isEqualTo(next);
    }

    @Test
    void toItem() {
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        User user = generator.nextObject(User.class);
        ItemRequest itemRequest = generator.nextObject(ItemRequest.class);
        Item item = ItemMapper.toItem(itemDto, user, itemRequest);
        assertThat(item.getItemRequest()).isEqualTo(itemRequest);
        assertThat(item.getName()).isEqualTo(itemDto.getName());
        assertThat(item.getOwner()).isEqualTo(user);
        assertThat(item.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(item.isAvailable()).isEqualTo(itemDto.getAvailable());
    }
}
