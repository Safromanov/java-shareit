package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping
    public UserDto createUser(@RequestBody UserDto user){
        return userService.createUser(user);
    }

    @PatchMapping
    public UserDto updateUser(@RequestBody UserDto user){
        return userService.createUser(user);
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable long userId){
        return userService.getById(userId);
    }
}
