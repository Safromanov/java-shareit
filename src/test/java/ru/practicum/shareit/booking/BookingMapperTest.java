package ru.practicum.shareit.booking;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.booking.dto.BookingPostRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BookingMapperTest {

    private final EasyRandom generator = new EasyRandom();

    @Test
    void toBookingResponse() {
        Booking booking = generator.nextObject(Booking.class);
        BookingResponse bookingResponse = BookingMapper.toBookingResponse(booking);
        assertThat(bookingResponse.getId()).isEqualTo(booking.getId());
        assertThat(bookingResponse.getStart()).isEqualTo(booking.getStart());
        assertThat(bookingResponse.getEnd()).isEqualTo(booking.getEnd());
        assertThat(bookingResponse.getStatus()).isEqualTo(booking.getStatus());
        assertThat(bookingResponse.getItem()).isEqualTo(ItemMapper.toItemDto(booking.getItem()));
        assertThat(bookingResponse.getBooker()).isEqualTo(UserMapper.toUserDto(booking.getBooker()));
    }

    @Test
    void toBookingGetResponse() {
        Booking booking = generator.nextObject(Booking.class);
        BookingGetResponse bookingResponse = BookingMapper.toBookingGetResponse(booking);
        assertThat(bookingResponse.getId()).isEqualTo(booking.getId());
        assertThat(bookingResponse.getItemId()).isEqualTo(booking.getItem().getId());
        assertThat(bookingResponse.getBookerId()).isEqualTo(booking.getBooker().getId());
    }

    @Test
    void toBooking() {
        BookingPostRequest postBooking = generator.nextObject(BookingPostRequest.class);
        User booker = generator.nextObject(User.class);
        Item item = generator.nextObject(Item.class);
        Booking booking = BookingMapper.toBooking(postBooking, booker, item);
        assertThat(booking.getId()).isEqualTo(booking.getId());
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getItem()).isEqualTo(item);
    }
}