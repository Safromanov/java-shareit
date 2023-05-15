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
    @NotBlank(groups = Create.class)
    private final String name;
    @NotBlank(groups = Create.class)
    private final String description;
    @NotNull(groups = Create.class)
    private final Boolean available;
}