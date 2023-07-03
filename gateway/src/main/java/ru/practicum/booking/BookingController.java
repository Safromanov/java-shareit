package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingPostRequest;
import ru.practicum.errorHandler.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> booking(@Valid @RequestBody BookingPostRequest booking,
                                          @RequestHeader(USER_ID_HEADER) long bookerId) {
        return bookingClient.booking(booking, bookerId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable long bookingId,
                                             @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable long bookingId,
                                                 @RequestParam(value = "approved", required = false) boolean approved,
                                                 @RequestHeader(USER_ID_HEADER) long ownerId) {
        return bookingClient.approveBooking(bookingId, ownerId, approved);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader(USER_ID_HEADER) long ownerId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @RequestParam(defaultValue = "0") @Min(0) int from,
                                                        @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        State stateEnum;
        try {
            stateEnum = State.valueOf(state);
        } catch (Exception e) {
            throw new BadRequestException("Unknown state: " + state);
        }
        return bookingClient.getAllBookingsByOwner(ownerId, stateEnum, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingByBooker(@RequestHeader(USER_ID_HEADER) long bookerId,
                                                        @RequestParam(defaultValue = "ALL") State state,
                                                        @RequestParam(defaultValue = "0") @Min(0) int from,
                                                        @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        State stateEnum;
//        try {
//            stateEnum = State.valueOf(state);
//        } catch (Exception e) {
//            throw new BadRequestException("Unknown state: " + state);
//        }
        return bookingClient.getAllBookingForBooker(bookerId, state, from, size);
    }
}
