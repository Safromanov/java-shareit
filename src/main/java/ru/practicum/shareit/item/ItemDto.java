package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.marker.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
@Builder
@AllArgsConstructor
public class ItemDto {
    private long id;
    @NotBlank(groups = Create.class)
    private String name;
    @NotBlank(groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private BookingGetResponse lastBooking;
    private BookingGetResponse nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}