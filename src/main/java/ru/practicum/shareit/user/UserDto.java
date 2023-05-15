package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

/**
 * A DTO for the {@link User} entity
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserDto {
    @Setter
    private long id;
    private final String name;
    @Email
    private String email;
}