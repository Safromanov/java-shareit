package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping
    public UserDto updateUser(@RequestBody UserDto user){
        return userService.updateUser(user);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable long userId){
        return userService.getById(userId);
    }
}
