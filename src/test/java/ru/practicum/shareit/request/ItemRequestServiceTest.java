package ru.practicum.shareit.request;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @Mock
    UserRepository userRepo;

    @Test
    void createItemRequest() {
        EasyRandom generator = new EasyRandom();
        UserDto userDto = generator.nextObject(UserDto.class);
    }
}