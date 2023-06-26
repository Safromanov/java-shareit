package ru.practicum.shareit.item.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.QComment;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.errorHandler.exception.BadRequestException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User dont found"));

        var itemRequest = (itemDto.getRequestId() != null)
                ? itemRequestRepository.findById(itemDto.getRequestId()).orElse(null)
                : null;
        Item item = ItemMapper.toItem(itemDto, owner, itemRequest);
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
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItemsByUserId(long idOwner, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        QBooking qBooking = QBooking.booking;

        List<Item> items = itemRepository.findAllByOwnerId(idOwner, page).getContent();
        List<BookingGetResponse> lastBookingsOfOwner = StreamUtils.createStreamFromIterator(
                        bookingRepository.findAll(qBooking.item.owner.id.eq(idOwner)
                                        .and(qBooking.start.before(LocalDateTime.now())),
                                Sort.by("start").descending()).iterator())
                .map(BookingMapper::toBookingGetResponse).collect(Collectors.toList());
        List<BookingGetResponse> nextBookingsOfOwner = StreamUtils.createStreamFromIterator(
                        bookingRepository.findAll(qBooking.item.owner.id.eq(idOwner)
                                        .and(qBooking.start.after(LocalDateTime.now())),
                                Sort.by("start").ascending()).iterator())
                .map(BookingMapper::toBookingGetResponse).collect(Collectors.toList());

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
    @Transactional(readOnly = true)
    public ItemDto getById(long itemId, long userId) {
        PageRequest sortDescFirst = PageRequest.of(0, 1, Sort.by("start").descending());
        PageRequest sortAscFirst = PageRequest.of(0, 1, Sort.by("start").ascending());
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item ID dont found"));
        BooleanExpression exp = QComment.comment.item.id.eq(itemId);
        List<Comment> comments = (List<Comment>) commentRepository.findAll(exp);
        item.setComments(comments);
        QBooking qBooking = QBooking.booking;
        if (item.getOwner().getId() == userId) {
            var last = bookingRepository.findAll(qBooking.item.id.eq(itemId)
                            .and(qBooking.start.before(LocalDateTime.now()))
                            .and(qBooking.status.eq(Status.APPROVED)), sortDescFirst).stream()
                    .map(BookingMapper::toBookingGetResponse)
                    .findFirst();
            Optional<BookingGetResponse> next = bookingRepository.findAll(qBooking.item.id.eq(itemId)
                            .and(qBooking.start.after(LocalDateTime.now()))
                            .and(qBooking.status.eq(Status.APPROVED)), sortAscFirst).stream()
                    .map(BookingMapper::toBookingGetResponse)
                    .findFirst();
            return ItemMapper.toItemGetDto(item, last.orElse(null), next.orElse(null));
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findByItemNameOrDesc(String str, int from, int size) {
        if (str.isBlank()) return new ArrayList<>();
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return itemRepository.findByNameOrDescription(str, page)
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

        Comment comment = CommentMapper.commentFromDto(commentDto,
                bookings.get(0).getBooker(),
                bookings.get(0).getItem());
        return CommentMapper.commentToDto(commentRepository.save(comment));
    }
}
