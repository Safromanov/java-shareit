package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingPostRequestTest {
    @Autowired
    private JacksonTester<BookingPostRequest> json;

    @Autowired
    private final ObjectMapper objectMapper;

    private final EasyRandom generator = new EasyRandom();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testDeserialize() throws Exception {
        BookingPostRequest bookingPostRequest = generator.nextObject(BookingPostRequest.class);
        String content = objectMapper.writeValueAsString(bookingPostRequest);
        BookingPostRequest result = json.parseObject(content);

        assertThat(result.getItemId()).isEqualTo(bookingPostRequest.getItemId());
        assertThat(result.getStart()).isEqualTo(bookingPostRequest.getStart().format(formatter));
        assertThat(result.getEnd()).isEqualTo(bookingPostRequest.getEnd().format(formatter));
    }

}