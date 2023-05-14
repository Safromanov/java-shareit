package ru.practicum.shareit.user;

import java.util.Map;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto user) {
        return new User(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

//    public static Map<String, Object> toMap(User user){
//        return Map.of(
//                "user_id" , user.getId(),
//                "name", user.getName(),
//                "email",user.getEmail());
//    }
//
//    public static Map<String, Object> toMap(UserDto userDto){
//        return Map.of(
//                "user_id" , userDto.getId(),
//                "name", userDto.getName(),
//                "email",userDto.getEmail());
//    }


}
