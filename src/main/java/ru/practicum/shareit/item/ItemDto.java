package ru.practicum.shareit.item;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.marker.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
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