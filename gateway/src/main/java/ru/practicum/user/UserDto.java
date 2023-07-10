package ru.practicum.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Create;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserDto {
    private long id;
    @NotBlank(groups = Create.class)
    private final String name;
    @Email
    @NotNull(groups = Create.class)
    private String email;
}