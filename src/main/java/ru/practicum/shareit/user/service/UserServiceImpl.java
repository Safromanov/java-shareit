package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errorHandler.exception.AlreadyExistException;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        userDto.setId(user.getId());
        log.info("Created user - {}", userDto);
        return userDto;
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID dont found"));
        if (userDto.getEmail() != null) {
            if (userRepository.findByEmail(userDto.getEmail()).isPresent()
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

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUserById(long id) {
        userRepository.findById(id).ifPresentOrElse(userRepository::delete,
                () -> {
                    throw new NotFoundException("User ID for delete dont found");
                });
        log.info("Deleted user by id - {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return userRepository.findAll(page).map(UserMapper::toUserDto).getContent();
    }
}
