package ru.practicum.shareit.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.QBooking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.errorHandler.exception.BadRequestException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
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
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemDto createItem(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        Item item = ItemMapper.toItem(itemDto, owner);
        item = itemRepository.save(item);
        itemDto.setId(item.getId());
        log.info("Created item - {}", itemDto);
        return itemDto;
    }

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

    public void deleteItemById(long id) {
        itemRepository.findById(id).ifPresentOrElse(itemRepository::delete,
                () -> {
                    throw new NotFoundException("Item ID dont found");
                });
        log.info("Deleted item with id - {}", id);
    }

    public List<ItemDto> getAllItemsByUserId(long idOwner) {
        List<Item> items = itemRepository.getAllByOwnerId(idOwner);
        List<BookingGetResponse> lastBookingsOfOwner = bookingRepository.
                findByOwner_IdAndEndBeforeAndStatus(idOwner, LocalDateTime.now());
        List<BookingGetResponse> nextBookingsOfOwner = bookingRepository
                .findByOwner_IdAndStatusAndStartAfterOrderByStartAsc(idOwner, LocalDateTime.now());
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

    public ItemDto getById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item ID dont found"));
        if (item.getOwner().getId() == userId) {
            Optional<BookingGetResponse> last = bookingRepository
                    .findByItem_IdAndEndBeforeAndStatus(itemId, LocalDateTime.now()).stream().findFirst();
            Optional<BookingGetResponse> next = bookingRepository
                    .findByItem_IdAndStatusAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now()).stream().findFirst();
            return ItemMapper.toItemGetDto(item, last.orElse(null), next.orElse(null));
        }

        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> findByItemNameOrDesc(String str) {
        if (str.isBlank()) return new ArrayList<>();
        return itemRepository.findByNameOrDescription(str)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }


//    List<Booking> bookings = bookingRepository.findAllByBookerAndItemIdAndStatusAndEndOfBookingIsBefore(
//            userId, itemId, BookingStatus.APPROVED, LocalDateTime.now());

    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        QBooking qBooking = QBooking.booking;

        BooleanExpression exp = qBooking.booker.id.eq(userId)
                .and(qBooking.item.id.eq(itemId))
                .and(qBooking.status.eq(Status.APPROVED))
                .and(qBooking.end.after(LocalDateTime.now()));

        List<Booking> bookings = (List<Booking>) bookingRepository.findAll(exp);
        if (bookings.isEmpty()) {
            throw new BadRequestException("Вы не можете оставить комментарий.");
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Предмет с id: " + itemId + " не существует"));
        Comment comment = CommentMapper.commentFromDto(commentDto, user, item);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.commentToDto(commentRepository.save(comment));
    }
}
