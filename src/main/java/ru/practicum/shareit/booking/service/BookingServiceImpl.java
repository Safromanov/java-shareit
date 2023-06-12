package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingPostRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.errorHandler.exception.AlreadyBookingException;
import ru.practicum.shareit.errorHandler.exception.BadRequestException;
import ru.practicum.shareit.errorHandler.exception.IncorrectUserException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingResponse booking(BookingPostRequest bookingReq, long bookerId) {
        if (!bookingReq.getEnd().isAfter(bookingReq.getStart()))
            throw new BadRequestException("Incorrect time of end booking");

        Booking booking = BookingMapper
                .toBooking(bookingReq, getBooker(bookerId), getItem(bookingReq.getItemId(), bookerId));
        booking = bookingRepository.save(booking);
        log.info("Booking - {}", booking);
        return BookingMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking dont found"));

        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId)
            return BookingMapper.toBookingResponse(booking);
        throw new IncorrectUserException("Only owner or Booker");
    }

    @Override
    @Transactional
    public BookingResponse approveBooking(long bookingId, long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking dont found"));
        if (ownerId == booking.getBooker().getId())
            throw new NotFoundException("Only owner can approved");
        if (ownerId != booking.getItem().getOwner().getId())
            throw new BadRequestException("Only owner can approved");
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new AlreadyBookingException("Its already approved or rejected");
        }
        booking.setStatus(approved
                ? Status.APPROVED
                : Status.REJECTED);
        return BookingMapper.toBookingResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookingForUser(long userId, State state) {
        userRepository.findById(userId).orElseThrow(() -> new IncorrectUserException("User dont exist"));
        QBooking qBooking = QBooking.booking;
        BooleanExpression exp = qBooking.booker.id.eq(userId);
        return getAllBookingBy(exp, state, qBooking, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookingByOwner(long userId, State state) {
        userRepository.findById(userId).orElseThrow(() -> new IncorrectUserException("User dont exist"));
        QBooking qBooking = QBooking.booking;
        BooleanExpression exp = qBooking.item.owner.id.eq(userId);
        return getAllBookingBy(exp, state, qBooking, LocalDateTime.now());
    }

    private List<BookingResponse> getAllBookingBy(BooleanExpression exp,
                                                  State state,
                                                  QBooking qBooking,
                                                  LocalDateTime currentTime) {
        switch (state) {
            case PAST:
                exp = exp.and(qBooking.end.before(currentTime));
                break;
            case FUTURE:
                exp = exp.and(qBooking.start.after(currentTime));
                break;
            case CURRENT:
                exp = exp.and(qBooking.start.before(currentTime)
                        .and(qBooking.end.after(currentTime)));
                break;
            case WAITING:
                exp = exp.and(qBooking.status.eq(Status.WAITING));
                break;
            case REJECTED:
                exp = exp.and(qBooking.status.eq(Status.REJECTED));
                break;
            case ALL:
                break;
        }
        return ((Collection<Booking>) bookingRepository
                .findAll(exp))
                .stream().sorted((x, y) -> -x.getStart().compareTo(y.getStart())).map(BookingMapper::toBookingResponse)
                .collect(Collectors.toList());
    }


    private Item getItem(long id, long bookerId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item dont found"));
        if (!item.isAvailable())
            throw new AlreadyBookingException("Item not available");

        if (item.getOwner().getId() == bookerId)
            throw new IncorrectUserException("Owner cannot be a booker");
        return item;
    }

    private User getBooker(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User dont found"));
    }
}
