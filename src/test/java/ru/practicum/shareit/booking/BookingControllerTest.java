package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingPostRequest;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingServiceImpl bookingService;

    private EasyRandom generator;
    private final ObjectMapper objectMapper;

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private long id = 1;
    private BookingPostRequest bookingPostRequest;
    private BookingResponse bookingResponse;
    private List<BookingResponse> bookingResponseList;

    @BeforeEach
    void setUp() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(LocalDate.class, LocalDate::now)
                .randomize(Long.class, () -> id++);
        generator = new EasyRandom(parameters);
        bookingPostRequest = generator.nextObject(BookingPostRequest.class);
        bookingResponse = generator.nextObject(BookingResponse.class);
        bookingResponseList = generator.objects(BookingResponse.class, 5).collect(Collectors.toList());
    }

    @Test
    void booking() throws Exception {
        when(bookingService.booking(any(BookingPostRequest.class), anyLong()))
                .thenReturn(bookingResponse);
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, eq(anyLong()))
                        .content(objectMapper.writeValueAsString(bookingPostRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingResponse.getItem().getId()))
                .andExpect(jsonPath("$.start").value(bookingResponse.getStart().format(formatter)))
                .andExpect(jsonPath("$.end").value(bookingResponse.getEnd().format(formatter)));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingResponse);
        mockMvc.perform(get("/bookings/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, eq(anyLong())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingResponse.getItem().getId()))
                .andExpect(jsonPath("$.start").value(bookingResponse.getStart().format(formatter)))
                .andExpect(jsonPath("$.end").value(bookingResponse.getEnd().format(formatter)));
    }

    @Test
    void getAllBookingsByOwner() throws Exception {
        when(bookingService.getAllBookingsByOwner(anyLong(), any(State.class), anyInt(), anyInt()))
                .thenReturn(bookingResponseList);
        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingResponseList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(bookingResponseList.get(1).getId()));
    }

    @Test
    void getAllBookingByBooker() throws Exception {
        when(bookingService.getAllBookingForBooker(anyLong(), any(State.class), anyInt(), anyInt()))
                .thenReturn(bookingResponseList);
        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingResponseList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(bookingResponseList.get(1).getId()));
    }
}