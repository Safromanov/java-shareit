package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository requestRepo;
    private final UserRepository userRepo;


//Поступает запрос с id пользователя и описанием, нужно самостоятельно добавить время создания и id и вернуть dto
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto requestDto){
        User owner = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        //requestRepo.save(requestDto);
        return requestDto;
    }



}
