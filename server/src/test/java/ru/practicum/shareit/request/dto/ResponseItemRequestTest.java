package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ResponseItemRequestTest {

    @Autowired
    private JacksonTester<ResponseItemRequest> json;
    @Autowired
    private final ObjectMapper objectMapper;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testSerialize() throws Exception {
        ResponseItemRequest responseItemRequestTest = new ResponseItemRequest(1L, "d", LocalDateTime.now(), new ArrayList<>());
        JsonContent<ResponseItemRequest> result = json.write(responseItemRequestTest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(responseItemRequestTest.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(responseItemRequestTest.getCreated().format(formatter));
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(responseItemRequestTest.getDescription());
    }

    @Test
    void testDeserialize() throws Exception {
        ResponseItemRequest responseItemRequestTest = new ResponseItemRequest(1L, "d", LocalDateTime.now(), new ArrayList<>());
        String content = objectMapper.writeValueAsString(responseItemRequestTest);
        assertThat(this.json.parse(content).getObject().getCreated())
                .isEqualTo(responseItemRequestTest.getCreated().format(formatter));
        assertThat(this.json.parseObject(content).getId()).isEqualTo(responseItemRequestTest.getId());
    }
}