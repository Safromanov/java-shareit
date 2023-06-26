package ru.practicum.shareit.item.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    private final EasyRandom generator = new EasyRandom();
    private final User owner = generator.nextObject(User.class);
    private final Item item = generator.nextObject(Item.class);
    private final ItemDto itemDto = ItemMapper.toItemDto(item);
    private final Comment comment = generator.nextObject(Comment.class);

    Booking booking = generator.nextObject(Booking.class);

    @Test
    void createItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(itemRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(itemDto, owner, new ItemRequest()));

        ItemDto receivedItem = itemService.createItem(itemDto, owner.getId());
        assertEquals(itemDto, receivedItem);
        verify(userRepository, times(1)).findById(owner.getId());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItem() {
        ItemDto updateItem = itemDto;
        updateItem.setName("up");
        updateItem.setDescription("up");
        updateItem.setAvailable(!itemDto.getAvailable());

        when(itemRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenReturn(Optional.of(item));

        ItemDto receivedItem = itemService.updateItem(updateItem, anyLong(), anyLong());
        assertEquals(updateItem, receivedItem);
        verify(itemRepository, times(1)).findByIdAndOwnerId(anyLong(), anyLong());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void failUpdateItem() {
        ItemDto updateItem = itemDto;
        updateItem.setName("up");
        updateItem.setDescription("up");
        updateItem.setAvailable(!itemDto.getAvailable());

        when(itemRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.updateItem(updateItem, anyLong(), anyLong()));

        verify(itemRepository, times(1)).findByIdAndOwnerId(anyLong(), anyLong());
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void getAllItemsByUserId() {
        int from = 0;
        int size = 10;
        when(itemRepository.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(item)));
        when(bookingRepository.findAll(any(BooleanExpression.class), eq(Sort.by("start").descending()))).thenReturn(new ArrayList<>());
        when(bookingRepository.findAll(any(BooleanExpression.class), eq(Sort.by("start").ascending()))).thenReturn(new ArrayList<>());
        List<ItemDto> foundList = itemService.getAllItemsByUserId(eq(anyLong()), from, size);

        assertThat(foundList).hasSize(1);
        assertEquals(itemDto, foundList.get(0));

        verify(itemRepository, times(1)).findAllByOwnerId(anyLong(), any(Pageable.class));
        verify(bookingRepository, times(2)).findAll(any(BooleanExpression.class), any(Sort.class));
    }

    @Test
    void getById_fromOwner() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentRepository.findAll(any(BooleanExpression.class))).thenReturn(new ArrayList<>());

        when(bookingRepository.findAll(any(BooleanExpression.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(booking)));
        ItemDto foundItem = itemService.getById(item.getId(), item.getOwner().getId());

        assertEquals(itemDto.getId(), foundItem.getId());
        assertEquals(BookingMapper.toBookingGetResponse(booking).getId(), foundItem.getNextBooking().getId());
        assertEquals(BookingMapper.toBookingGetResponse(booking).getId(), foundItem.getLastBooking().getId());

        verify(itemRepository, times(1)).findById(anyLong());
        verify(commentRepository, times(1)).findAll(any(BooleanExpression.class));

        verify(bookingRepository, times(2)).findAll(any(BooleanExpression.class), any(PageRequest.class));
    }

    @Test
    void getById_fromNotOwner() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentRepository.findAll(any(BooleanExpression.class))).thenReturn(new ArrayList<>());
        ItemDto foundItem = itemService.getById(item.getId(), item.getOwner().getId() - 1);

        assertEquals(itemDto.getId(), foundItem.getId());
        assertNull(foundItem.getNextBooking());
        assertNull(foundItem.getLastBooking());

        verify(itemRepository, times(1)).findById(anyLong());
        verify(commentRepository, times(1)).findAll(any(BooleanExpression.class));

        verify(bookingRepository, never()).findAll(any(BooleanExpression.class), any(PageRequest.class));
    }

    @Test
    void findByItemNameOrDesc() {
        when(itemRepository.findByNameOrDescription(anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(item)));
        List<ItemDto> foundList = itemService.findByItemNameOrDesc(item.getName(), 0, 10);
        assertThat(foundList).hasSize(1);
        assertEquals(itemDto, foundList.get(0));
    }

    @Test
    void addComment() {
        when(bookingRepository.findAll(any(BooleanExpression.class)))
                .thenReturn(List.of(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        var commentDto = itemService.addComment(anyLong(), booking.getItem().getId(), CommentMapper.commentToDto(comment));

        assertEquals(CommentMapper.commentToDto(comment), commentDto);
    }
}