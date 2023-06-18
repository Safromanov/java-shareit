package ru.practicum.shareit.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createRequest(@RequestBody @Valid ItemRequestDto request) {
        return null;
    }
}
