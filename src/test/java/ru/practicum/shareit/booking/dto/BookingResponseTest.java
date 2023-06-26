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
class BookingResponseTest {
    @Autowired
    private JacksonTester<BookingResponse> json;

    @Autowired
    private final ObjectMapper objectMapper;

    private final EasyRandom generator = new EasyRandom();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testDeserialize() throws Exception {
        BookingResponse bookingResponse = generator.nextObject(BookingResponse.class);
        String content = objectMapper.writeValueAsString(bookingResponse);
        assertThat(this.json.parseObject(content).getId())
                .isEqualTo(bookingResponse.getId());
        assertThat(this.json.parseObject(content).getStatus())
                .isEqualTo(bookingResponse.getStatus());
        assertThat(this.json.parseObject(content).getStart()).isEqualTo(bookingResponse.getStart().format(formatter));
        assertThat(this.json.parseObject(content).getEnd()).isEqualTo(bookingResponse.getEnd().format(formatter));
    }
}