package ru.practicum.shareit.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(@Valid UserDto user){
        if (countByEmail(user.getEmail()) > 0) throw new RuntimeException("Пользователь с таким email уже существует");
        userRepository.save(UserMapper.toUser(user));
        return user;
    }

    public User getById(long id){
        return userRepository.getById(id);
    }

    private long countByEmail(String email) {
        return userRepository.countByEmail(email);
    }
}
