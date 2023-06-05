package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.marker.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
@Builder
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    long id;
    @NotBlank(groups = Create.class)
    String name;
    @NotBlank(groups = Create.class)
    String description;
    @NotNull(groups = Create.class)
    Boolean available;
    BookingGetResponse lastBooking;
    BookingGetResponse nextBooking;
    List<CommentDto> comments;
}