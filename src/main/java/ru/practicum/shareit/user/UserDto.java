package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
  //  @Nullable

  //  @NotNull(groups = CreateUser.class)
    private final String name;

    @Email
   // @NotNull(groups = CreateUser.class)
    private String email;
}