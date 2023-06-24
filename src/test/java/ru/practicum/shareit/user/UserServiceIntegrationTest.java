package ru.practicum.shareit.user;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.errorHandler.exception.AlreadyExistException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    private final EasyRandom generator = new EasyRandom();

    private List<UserDto> expectedListUsers;

    @BeforeEach
    public void createUsers() {
        expectedListUsers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            UserDto userDto = generator.nextObject(UserDto.class);
            userDto.setEmail("test" + i + "@test.oki");
            expectedListUsers.add(userDto);
            userService.createUser(userDto);
        }
    }

    @Test
    public void testGetUser() {
        assertEquals(expectedListUsers.get(1), userService.getById(2));
        assertThrows(NotFoundException.class, () -> userService.getById(expectedListUsers.size() + 1));
    }

    @Test
    public void testUpdateUser() {
        var updateUser = expectedListUsers.get(0);
        updateUser.setEmail("update@update");
        assertEquals(updateUser,
                userService.updateUser(updateUser, 1));

        assertThrows(NotFoundException.class, ()
                -> userService.updateUser(expectedListUsers.get(0),
                expectedListUsers.size() + 1));

        assertThrows(AlreadyExistException.class, ()
                -> userService.updateUser(expectedListUsers.get(0), expectedListUsers.size()));
    }

    @Test
    public void testDeleteUserById() {
        userService.deleteUserById(1);
        assertThrows(NotFoundException.class, () -> userService.getById(1));
    }

    @Test
    public void testGetAllUsers() {
        assertEquals(expectedListUsers,
                userService.getAllUsers(0, 10));

        assertEquals(expectedListUsers.subList(0, 1),
                userService.getAllUsers(0, 1));
    }
}
