package ru.practicum.shareit.booking.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingPostRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.errorHandler.exception.AlreadyBookingException;
import ru.practicum.shareit.errorHandler.exception.BadRequestException;
import ru.practicum.shareit.errorHandler.exception.IncorrectUserException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingService bookingService;
    private final EasyRandom generator = new EasyRandom();

    private Item item;
    private User owner;
    private User booker;
    private BookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(new User("testName", "test@email.ops"));
        booker = userRepository.save(new User("bookerName", "booker@email.ops"));
        item = itemRepository.save(new Item("name", "desc", true, owner, new ArrayList<>(), null));
        BookingPostRequest bookingPostRequest = new BookingPostRequest(LocalDateTime.now(),
                LocalDateTime.now().plusDays(1), item.getId());
        bookingResponse = bookingService.booking(bookingPostRequest, booker.getId());

    }

    @Test
    void booking() {
        assertEquals(1, bookingResponse.getId());
        assertEquals(UserMapper.toUserDto(booker), bookingResponse.getBooker());
        assertEquals(1, bookingResponse.getItem().getId());
        assertEquals(Status.WAITING, bookingResponse.getStatus());
        assertNotNull(bookingResponse.getStart());
        assertNotNull(bookingResponse.getEnd());
    }

    @Test
    void booking_incorrectDate() {
        BookingPostRequest bookingPostRequestWithBadDate = new BookingPostRequest(LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1), item.getId());
        assertThrows(BadRequestException.class, () -> bookingService.booking(bookingPostRequestWithBadDate, booker.getId()));
    }

    @Test
    void getBooking_fromOwnerAndBooker() {
        BookingResponse bookingResponseOwner = bookingService.getBooking(bookingResponse.getId(), owner.getId());
        BookingResponse bookingResponseBooker = bookingService.getBooking(bookingResponse.getId(), owner.getId());
        assertEquals(bookingResponse.getId(), bookingResponseOwner.getId());
        assertEquals(bookingResponse.getId(), bookingResponseBooker.getId());
    }

    @Test
    void getBooking_fromNotOwnerOrBooker() {
        assertThrows(IncorrectUserException.class, () -> bookingService.getBooking(bookingResponse.getId(),
                booker.getId() + owner.getId()));
    }

    @Test
    void approveBooking_Approved() {
        BookingResponse bookingResponseApproved =
                bookingService.approveBooking(bookingResponse.getId(), owner.getId(), true);
        assertEquals(Status.APPROVED, bookingResponseApproved.getStatus());
    }

    @Test
    void approveBooking_rejected() {
        BookingResponse bookingResponseApproved =
                bookingService.approveBooking(bookingResponse.getId(), owner.getId(), false);
        assertEquals(Status.REJECTED, bookingResponseApproved.getStatus());

        assertThrows(AlreadyBookingException.class, () ->
                bookingService.approveBooking(bookingResponse.getId(), owner.getId(), false));
    }

    @Test
    void approveBooking_fromNotOwner() {
        assertThrows(NotFoundException.class, () ->
                bookingService.approveBooking(bookingResponse.getId(), booker.getId(), false));
    }

    @Test
    void getAllBookingForBooker() {
        List<BookingResponse> bookingResponseList =
                bookingService.getAllBookingForBooker(booker.getId(), State.CURRENT, 0, 10);
        assertEquals(bookingResponse.getId(), bookingResponseList.get(0).getId());
        assertTrue(bookingResponseList.get(0).getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookingResponseList.get(0).getEnd().isAfter(LocalDateTime.now()));

        bookingResponseList =
                bookingService.getAllBookingForBooker(booker.getId(), State.ALL, 0, 10);
        assertEquals(bookingResponse.getId(), bookingResponseList.get(0).getId());

        bookingResponseList =
                bookingService.getAllBookingForBooker(booker.getId(), State.WAITING, 0, 10);
        assertEquals(bookingResponse.getId(), bookingResponseList.get(0).getId());
        assertEquals(Status.WAITING, bookingResponseList.get(0).getStatus());
    }

    @Test
    void getAllBookingsByOwner() {
        Booking bookingNext =
                bookingRepository.save(new Booking(item, booker, Status.WAITING,
                        LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)));
        List<BookingResponse> bookingResponseList =
                bookingService.getAllBookingsByOwner(owner.getId(), State.FUTURE, 0, 10);

        assertEquals(bookingNext.getId(), bookingResponseList.get(0).getId());

        Booking bookingLast =
                bookingRepository.save(new Booking(item, booker, Status.WAITING,
                        LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)));

        bookingResponseList =
                bookingService.getAllBookingsByOwner(owner.getId(), State.PAST, 0, 10);

        assertEquals(bookingLast.getId(), bookingResponseList.get(0).getId());

        bookingResponseList =
                bookingService.getAllBookingsByOwner(owner.getId(), State.CURRENT, 0, 10);

        assertEquals(bookingResponse.getId(), bookingResponseList.get(0).getId());
        assertTrue(bookingResponseList.get(0).getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookingResponseList.get(0).getEnd().isAfter(LocalDateTime.now()));

        bookingResponseList =
                bookingService.getAllBookingsByOwner(owner.getId(), State.ALL, 0, 10);
        assertEquals(3, bookingResponseList.size());

        bookingResponseList =
                bookingService.getAllBookingsByOwner(owner.getId(), State.WAITING, 0, 10);
        assertEquals(3, bookingResponseList.size());
        assertEquals(Status.WAITING, bookingResponseList.get(0).getStatus());
    }
}