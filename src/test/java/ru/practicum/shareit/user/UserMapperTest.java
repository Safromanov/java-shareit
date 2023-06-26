package ru.practicum.shareit.user;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class UserMapperTest {

    private final EasyRandom generator = new EasyRandom();

    @Test
    public void userToDtoTest() {
        User user = generator.nextObject(User.class);
        UserDto userDto = UserMapper.toUserDto(user);
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void userFromDtoTest() {
        UserDto userDto = generator.nextObject(UserDto.class);
        User user = UserMapper.toUser(userDto);
        assertThat(user.getName()).isEqualTo(userDto.getName());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }
}
