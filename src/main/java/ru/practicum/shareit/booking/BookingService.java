package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorHandler.exception.AlreadyBookingException;
import ru.practicum.shareit.errorHandler.exception.IncorrectUserException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.errorHandler.exception.TimeException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingResponse booking(BookingRequest bookingReq, long bookerId) {
        if (!bookingReq.getEnd().isAfter(bookingReq.getStart()))
            throw new TimeException("Incorrect time of end booking");

        Item item = itemRepository.findById(bookingReq.getItemId())
                .orElseThrow(() -> new NotFoundException("Item dont found"));

        if (!item.isAvailable())
            throw new AlreadyBookingException("Item not available");

        if (item.getOwner().getId() == bookerId)
            throw new IncorrectUserException("Owner cannot be a booker");
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("User dont found"));

        Booking booking = BookingMapper.toBooking(bookingReq, booker, item);
        booking = bookingRepository.save(booking);
        log.info("Booking - {}", booking);
        return BookingMapper.toBookingResponse(booking);
    }
}
