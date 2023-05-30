package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public BookingResponse booking(@Valid @RequestBody BookingRequest booking, @RequestHeader(USER_ID_HEADER) long bookerId) {
        return bookingService.booking(booking, bookerId);
    }

//    @PatchMapping("/{BookingId}")
//    public ItemDto updateItem(@RequestBody @Valid ItemDto itemDto,
//                              @PathVariable long itemId,
//                              @RequestHeader(USER_ID_HEADER) long userId) {
//        return bookingService.updateUser(itemDto, itemId, userId);
//    }

}
