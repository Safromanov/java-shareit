package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.booking.dto.BookingPostRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

@RequiredArgsConstructor
public class BookingMapper {
    public static BookingResponse toBookingResponse(Booking booking) {
        return new BookingResponse(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDto(booking.getItem()),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getStatus());
    }

    public static BookingGetResponse toBookingGetResponse(Booking booking) {
        return new BookingGetResponse(booking.getId(),
                booking.getItem().getId(),
                booking.getBooker().getId());
    }

    public static Booking toBooking(BookingPostRequest dto, User booker, Item item) {
        return new Booking(
                item,
                booker,
                Status.WAITING,
                dto.getStart(),
                dto.getEnd()
        );
    }
}
