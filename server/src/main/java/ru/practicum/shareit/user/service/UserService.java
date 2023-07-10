package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, long userId);

    UserDto getById(long id);

    void deleteUserById(long id);

    List<UserDto> getAllUsers(int from, int size);
}
