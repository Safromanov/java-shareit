package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorHandler.exception.AlreadyExistException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        if (countByEmail(userDto.getEmail()) > 0) throw new AlreadyExistException("Email has already been taken");
        User user = userRepository.save(UserMapper.toUser(userDto));
        userDto.setId(user.getId());
        log.info("Created user - {}", userDto);
        return userDto;
    }

    public UserDto updateUser(UserDto userDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID dont found"));
        if (userDto.getEmail() != null) {
            if (countByEmail(userDto.getEmail()) > 0
                    && !Objects.equals(user.getEmail(), userDto.getEmail()))
                throw new AlreadyExistException("Email has already been taken");
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null)
            user.setName(userDto.getName());
        userDto = UserMapper.toUserDto(userRepository.save(user));
        log.info("Update user - {}", userDto);
        return UserMapper.toUserDto(user);
    }

    public UserDto getById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        return UserMapper.toUserDto(user);
    }

    public void deleteUserById(long id) {
        try {
            userRepository.deleteById(id);
            log.info("Deleted user by id - {}", id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User ID for delete dont found");
        }
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    private long countByEmail(String email) {
        return userRepository.countByEmail(email);
    }
}
