package ru.practicum.shareit.item.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.comment.*;
import ru.practicum.shareit.errorHandler.exception.BadRequestException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        Item item = ItemMapper.toItem(itemDto, owner);
        item = itemRepository.save(item);
        itemDto.setId(item.getId());
        log.info("Created item - {}", itemDto);
        return itemDto;
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {
        Item item = itemRepository.findByIdAndOwnerId(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Item ID dont found"));
        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        itemRepository.save(item);
        log.info("Updated item - {}", itemDto);
        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public void deleteItemById(long id) {
        itemRepository.findById(id).ifPresentOrElse(itemRepository::delete,
                () -> {
                    throw new NotFoundException("Item ID dont found");
                });
        log.info("Deleted item with id - {}", id);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(long idOwner) {
        List<Item> items = itemRepository.findByOwnerId(idOwner);
        List<BookingGetResponse> lastBookingsOfOwner = bookingRepository
                .findApprovedByOwnerIdAndEndBefore(idOwner, LocalDateTime.now());
        List<BookingGetResponse> nextBookingsOfOwner = bookingRepository
                .findApprovedByOwnerIdAndStartAfter(idOwner, LocalDateTime.now());
        List<ItemDto> ownerItems = new ArrayList<>();
        for (Item item : items) {
            ownerItems.add(
                    ItemMapper.toItemGetDto(item,
                            lastBookingsOfOwner.stream()
                                    .filter(x -> x.getItemId() == item.getId()).findFirst().orElse(null),
                            nextBookingsOfOwner.stream()
                                    .filter(x -> x.getItemId() == item.getId()).findFirst().orElse(null)));
        }
        return ownerItems;
    }

    @Override
    public ItemDto getById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item ID dont found"));
        BooleanExpression exp = QComment.comment.item.id.eq(itemId);
        List<Comment> comments = (List<Comment>) commentRepository.findAll(exp);
        item.setComments(comments);
        if (item.getOwner().getId() == userId) {
            Optional<BookingGetResponse> last = bookingRepository
                    .findApprovedByItemIdAndBefore(itemId, LocalDateTime.now()).stream().findFirst();
            Optional<BookingGetResponse> next = bookingRepository
                    .findApprovedByItemIdAndStartAfter(itemId, LocalDateTime.now()).stream().findFirst();
            return ItemMapper.toItemGetDto(item, last.orElse(null), next.orElse(null));
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findByItemNameOrDesc(String str) {
        if (str.isBlank()) return new ArrayList<>();
        return itemRepository.findByNameOrDescription(str)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        QBooking qBooking = QBooking.booking;

        BooleanExpression exp = qBooking.booker.id.eq(userId)
                .and(qBooking.item.id.eq(itemId))
                .and(qBooking.status.eq(Status.APPROVED))
                .and(qBooking.end.before(LocalDateTime.now()));

        List<Booking> bookings = (List<Booking>) bookingRepository.findAll(exp);

        if (bookings.isEmpty()) {
            throw new BadRequestException("Only those who have previously booked can send a message");
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User ID dont found"));
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Item ID dont found"));

        Comment comment = CommentMapper.commentFromDto(commentDto, user, item);
        return CommentMapper.commentToDto(commentRepository.save(comment));
    }
}
