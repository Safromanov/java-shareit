package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * A DTO for the {@link User} entity
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto implements Serializable {
    private final long id;
    @NotBlank
    private final String name;
    @Email
    private final String email;
}