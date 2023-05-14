package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto user){
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto user, @PathVariable long userId){
        return userService.updateUser(user, userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUserById( @PathVariable long userId){
        userService.deleteUserById(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable long userId){
        return userService.getById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }
}
