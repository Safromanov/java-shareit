package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingPostRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingResponse booking(BookingPostRequest bookingReq, long bookerId);

    BookingResponse getBooking(long bookingId, long userId);

    BookingResponse approveBooking(long bookingId, long ownerId, boolean approved);

    List<BookingResponse> getAllBookingForUser(long userId, State state, int from, int size);

    List<BookingResponse> getAllBookingsByOwner(long userId, State state, int from, int size);
}
