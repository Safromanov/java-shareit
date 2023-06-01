package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.dto.BookingGetResponse;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {
    BookingGetResponse findFirstByItem_IdAndEndBeforeAndItem_AvailableTrueOrderByStartAsc(long id, LocalDateTime date);

    BookingGetResponse findFirstByItem_IdAndEndBeforeAndItem_AvailableTrueOrderByEndDesc(long id, LocalDateTime date);


}