package ru.practicum.shareit.user;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.errorHandler.exception.AlreadyExistException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository mockUserRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private final EasyRandom generator = new EasyRandom();
    private final User user = generator.nextObject(User.class);
    private static final int PAGE_SIZE = 10;

    @Test
    void createUser() {
        UserDto userDtoIn = generator.nextObject(UserDto.class);

        when(mockUserRepository.save(any(User.class)))
                .then(returnsFirstArg());

        assertEquals(userDtoIn, userService.createUser(userDtoIn));

        verify(mockUserRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void getById() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        UserDto found = userService.getById(generator.nextLong());

        assertEquals(user.getId(), found.getId());
        assertEquals(user.getName(), found.getName());
        assertEquals(user.getEmail(), found.getEmail());

        verify(mockUserRepository, times(1))
                .findById(anyLong());
    }

    @Test
    void getAllUsers() {
        User user2 = generator.nextObject(User.class);

        when(mockUserRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user, user2)));

        List<UserDto> foundList = userService.getAllUsers(0, PAGE_SIZE);

        assertThat(foundList).hasSize(2);
        assertEquals(UserMapper.toUserDto(user), foundList.get(0));
        assertEquals(UserMapper.toUserDto(user2), foundList.get(1));
        verify(mockUserRepository, times(1))
                .findAll(PageRequest.of(0, PAGE_SIZE));
    }

    @Test
    void delete_whenUserExist() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        userService.deleteUserById(generator.nextLong());
        verify(mockUserRepository, times(1)).findById(anyLong());
        verify(mockUserRepository, times(1)).delete(any(User.class));
    }

    @Test
    void delete_whenUserDontExist() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> userService.deleteUserById(generator.nextLong()));

        verify(mockUserRepository, times(1)).findById(anyLong());
        verify(mockUserRepository, never()).delete(any(User.class));
    }

    @Test
    void updateUser() {
        UserDto expectedUserDto = UserMapper.toUserDto(user);

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(mockUserRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(mockUserRepository.save(any(User.class)))
                .then(returnsFirstArg());

        assertEquals(expectedUserDto, userService.updateUser(expectedUserDto, user.getId()));
        verify(mockUserRepository, times(1)).findByEmail(anyString());
        verify(mockUserRepository, times(1)).findById(anyLong());
        verify(mockUserRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_withRepeatedEmail() {
        User user2 = generator.nextObject(User.class);
        user2.setEmail(user.getEmail());
        UserDto expectedUserDto = UserMapper.toUserDto(user);

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(mockUserRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user2));

        assertThrows(AlreadyExistException.class,
                () -> userService.updateUser(expectedUserDto, user.getId()));

        verify(mockUserRepository, times(1)).findByEmail(anyString());
        verify(mockUserRepository, times(1)).findById(anyLong());
        verify(mockUserRepository, never()).save(any(User.class));
    }
}