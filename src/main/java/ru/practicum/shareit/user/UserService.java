package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.errorhandler.excaption.AlreadyExistException;


import javax.validation.Valid;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(@Valid UserDto userDto){
        if (countByEmail(userDto.getEmail()) > 0) throw new AlreadyExistException("Email has already been taken");
        User user = userRepository.save(UserMapper.toUser(userDto));
        userDto.setId(user.getId());
        return userDto;
    }

    public UserDto updateUser(@Valid UserDto userDto){
     //   User user =
        userRepository.updateUser(UserMapper.toUser(userDto));
      //  userDto.setId(user.getId());
        return userDto;
    }

    public UserDto getById(long id){
        return UserMapper.toUserDto(userRepository.getById(id));
    }

    private long countByEmail(String email) {
        return userRepository.countByEmail(email);
    }
}
