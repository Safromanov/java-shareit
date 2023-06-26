package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> json;
    @Autowired
    private final ObjectMapper objectMapper;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto(
                1,
                "test",
                "test@test.om");

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) userDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }

    @Test
    void testDeserialize() throws Exception {
        UserDto userDto = new UserDto(
                1,
                "test",
                "test@test.om");
        String content = objectMapper.writeValueAsString(userDto);
        assertThat(this.json.parse(content).getObject())
                .isEqualTo(userDto);
        assertThat(this.json.parseObject(content).getName()).isEqualTo("test");
    }
}
