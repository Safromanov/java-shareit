package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingPostRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponse booking(@Valid @RequestBody BookingPostRequest booking,
                                   @RequestHeader(USER_ID_HEADER) long ownerId) {
        return bookingService.booking(booking, ownerId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@PathVariable long bookingId,
                                      @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse a(@PathVariable long bookingId,
                             @RequestParam(value = "approved", required = false) boolean approved,
                             @RequestHeader(USER_ID_HEADER) long ownerId) {
        return bookingService.approveBooking(bookingId, ownerId, approved);
    }

    @GetMapping("/owner")
    public List<BookingResponse> getAllBookingByOwner(@RequestHeader(USER_ID_HEADER) long ownerId,
                                                      @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getAllBookingByOwner(ownerId, state);
    }

    @GetMapping
    public List<BookingResponse> getAllBookingByBooker(@RequestHeader(USER_ID_HEADER) long bookerId,
                                                       @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getAllBookingForUser(bookerId, state);
    }

}
