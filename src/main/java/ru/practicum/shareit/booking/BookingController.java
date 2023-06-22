package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingPostRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
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
    public BookingResponse approveBooking(@PathVariable long bookingId,
                                          @RequestParam(value = "approved", required = false) boolean approved,
                                          @RequestHeader(USER_ID_HEADER) long ownerId) {
        return bookingService.approveBooking(bookingId, ownerId, approved);
    }

    @GetMapping("/owner")
    public List<BookingResponse> getAllBookingsByOwner(@RequestHeader(USER_ID_HEADER) long ownerId,
                                                       @RequestParam(defaultValue = "ALL") State state,
                                                       @RequestParam(defaultValue = "0") @Min(0) int from,
                                                       @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return bookingService.getAllBookingsByOwner(ownerId, state, from, size);
    }

    @GetMapping
    public List<BookingResponse> getAllBookingByBooker(@RequestHeader(USER_ID_HEADER) long bookerId,
                                                       @RequestParam(defaultValue = "ALL") State state,
                                                       @RequestParam(defaultValue = "0") @Min(0) int from,
                                                       @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return bookingService.getAllBookingForUser(bookerId, state, from, size);
    }
}
