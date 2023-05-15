package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.errorhandler.exception.AlreadyExistException;
import ru.practicum.shareit.errorhandler.exception.NotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(@Valid UserDto userDto) {
        if (countByEmail(userDto.getEmail()) > 0) throw new AlreadyExistException("Email has already been taken");
        User user = userRepository.save(UserMapper.toUser(userDto));
        userDto.setId(user.getId());
        return userDto;
    }

    public UserDto updateUser(@Valid UserDto userDto, long userId) {
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
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    public UserDto getById(long id) {
        return UserMapper.toUserDto(userRepository.getById(id));
    }

    public void deleteUserById(long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("User ID dont found");
        }
    }

    public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    private long countByEmail(String email) {
        return userRepository.countByEmail(email);
    }

}
