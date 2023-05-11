package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ItemDto {
    private final long id;
    @NotBlank
    private final String name;
    @NotBlank
    private final String description;
    private final Boolean available;
}