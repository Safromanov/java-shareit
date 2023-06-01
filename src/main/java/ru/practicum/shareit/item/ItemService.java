package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.errorHandler.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    //    @PersistenceContext
//    private final EntityManager entityManager;
    private final BookingRepository bookingRepository;
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        Item item = ItemMapper.toItem(itemDto, owner);
        item = itemRepository.save(item);
        itemDto.setId(item.getId());
        log.info("Created item - {}", itemDto);
        return itemDto;
    }

    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {
        Item item = itemRepository.findByIdAndOwnerId(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Item ID dont found"));
        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        itemRepository.save(item);
        log.info("Updated item - {}", itemDto);
        return ItemMapper.toItemDto(item);
    }

    public void deleteItemById(long id) {
        itemRepository.findById(id).ifPresentOrElse(itemRepository::delete,
                () -> {
                    throw new NotFoundException("Item ID dont found");
                });
        log.info("Deleted item with id - {}", id);
    }

    public List<ItemDto> getAllItemsByUserId(long id) {
        return itemRepository.getAllByOwnerId(id).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public ItemDto getById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item ID dont found"));
        if (item.getOwner().getId() == userId) {
//            QBooking qBooking = QBooking.booking;
//            BooleanExpression exp = qBooking.item.owner.id.eq(userId)
//                    .and(qBooking.start.before(LocalDateTime.now())
//                            .and(qBooking.status.eq(Status.APPROVED)));
//            JPAQuery<Booking> query = new JPAQuery<>(entityManager);
//            var last = query.from(qBooking).where(qBooking.item.owner.id.eq(userId)
//                            .and(qBooking.start.before(LocalDateTime.now())
//                                    .and(qBooking.status.eq(Status.APPROVED))))
//                    .orderBy(qBooking.start.desc()).stream()
//                    .findFirst()
//                    .orElse(null);
//            var next = query.from(qBooking).where(qBooking.item.owner.id.eq(userId)
//                            .and(qBooking.end.after(LocalDateTime.now())
//                                    .and(qBooking.status.eq(Status.APPROVED))))
//                    .orderBy(qBooking.start.asc())
//                    .stream()
//                    .findFirst()
//                    .orElse(null);
            var last = bookingRepository
                    .findFirstByItem_IdAndEndBeforeAndItem_AvailableTrueOrderByEndDesc(itemId, LocalDateTime.now());
            var next = bookingRepository
                    .findFirstByItem_IdAndEndBeforeAndItem_AvailableTrueOrderByStartAsc(itemId, LocalDateTime.now());
            return ItemMapper.toItemGetDto(item, last, next);
        }

        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> findByItemNameOrDesc(String str) {
        if (str.isBlank()) return new ArrayList<>();
        return itemRepository.findByNameOrDescription(str)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
