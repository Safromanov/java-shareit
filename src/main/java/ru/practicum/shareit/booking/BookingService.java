package ru.practicum.shareit.booking;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingPostRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.errorHandler.exception.AlreadyBookingException;
import ru.practicum.shareit.errorHandler.exception.IncorrectUserException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.errorHandler.exception.TimeException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public BookingResponse booking(BookingPostRequest bookingReq, long bookerId) {
        if (!bookingReq.getEnd().isAfter(bookingReq.getStart()))
            throw new TimeException("Incorrect time of end booking");

        Booking booking = BookingMapper.toBooking(bookingReq, getBooker(bookerId), getItem(bookingReq.getItemId(), bookerId));
        booking = bookingRepository.save(booking);
        log.info("Booking - {}", booking);
        return BookingMapper.toBookingResponse(booking);
    }

    public BookingResponse getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking dont found"));
        //  if (booking.getItem().getOwner().getId() == userId) {

        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId)
            return BookingMapper.toBookingResponse(booking);
        throw new IncorrectUserException("Only owner or Booker");
    }

    public BookingResponse updateBooking(long bookingId, long bookerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking dont found"));
        if (bookerId != booking.getItem().getOwner().getId())
            throw new IncorrectUserException("Only owner can approved");
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new AlreadyBookingException("Its already approved or rejected");
        }
        booking.setStatus(approved
                ? Status.APPROVED
                : Status.REJECTED);
        return BookingMapper.toBookingResponse(bookingRepository.save(booking));
    }

    public List<BookingResponse> getAllBookingForUser(long userId, State state) {
        userRepository.findById(userId).orElseThrow(() -> new IncorrectUserException("User dont exist"));
        QBooking qBooking = QBooking.booking;
        BooleanExpression byBookerId = qBooking.booker.id.eq(userId);

        JPAQuery<Booking> query = new JPAQuery<>(entityManager);

        if (state == State.PAST)
            byBookerId = byBookerId.and(qBooking.end.before(LocalDateTime.now()));
        if (state == State.FUTURE)
            byBookerId = byBookerId.and(qBooking.start.after(LocalDateTime.now()));
        if (state == State.CURRENT)
            byBookerId = byBookerId
                    .and(qBooking.start.before(LocalDateTime.now())
                            .and(qBooking.end.after(LocalDateTime.now())));
        if (state == State.WAITING)
            byBookerId = byBookerId
                    .and(qBooking.status.eq(Status.WAITING));
        if (state == State.REJECTED)
            byBookerId = byBookerId
                    .and(qBooking.status.eq(Status.REJECTED));

        return ((Collection<Booking>) bookingRepository
                .findAll(byBookerId))
                .stream()
                .sorted((x, y) -> -x.getStart().compareTo(y.getStart()))
                .map(BookingMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getAllBookingByOwner(long userId, State state) {
        userRepository.findById(userId).orElseThrow(() -> new IncorrectUserException("User dont exist"));
        QBooking qBooking = QBooking.booking;
        BooleanExpression byBookerId = qBooking.item.owner.id.eq(userId);

        if (state == State.PAST)
            byBookerId = byBookerId.and(qBooking.end.before(LocalDateTime.now()));
        if (state == State.FUTURE)
            byBookerId = byBookerId.and(qBooking.start.after(LocalDateTime.now()));
        if (state == State.CURRENT)
            byBookerId = byBookerId
                    .and(qBooking.start.before(LocalDateTime.now())
                            .and(qBooking.end.after(LocalDateTime.now())));
        if (state == State.WAITING)
            byBookerId = byBookerId
                    .and(qBooking.status.eq(Status.WAITING));
        if (state == State.REJECTED)
            byBookerId = byBookerId
                    .and(qBooking.status.eq(Status.REJECTED));

        return ((Collection<Booking>) bookingRepository
                .findAll(byBookerId))
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
