package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ItemDto {
    private long id;

    private final String name;

    private final String description;
    @NotNull
    private final Boolean available;
}