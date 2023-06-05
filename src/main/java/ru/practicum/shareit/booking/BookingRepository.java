package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.dto.BookingGetResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {

    @Query("select b from Booking b where b.item.id = ?1 and  b.end < ?2 " +
            "and  b.status = ru.practicum.shareit.booking.Status.APPROVED " +
            "order by  b.start desc")
    List<BookingGetResponse> findByItem_IdAndEndBeforeAndStatus(long id, LocalDateTime end);

    @Query("select b from Booking b where b.item.id = ?1 " +
            "and b.status = ru.practicum.shareit.booking.Status.APPROVED " +
            "and b.start > ?2 " +
            "order by b.start")
    List<BookingGetResponse> findByItem_IdAndStatusAndStartAfterOrderByStartAsc(long id, LocalDateTime start);

    @Query("select b from Booking b where b.item.owner.id = ?1 and  b.end < ?2 " +
            "and  b.status = ru.practicum.shareit.booking.Status.APPROVED " +
            "order by  b.start desc")
    List<BookingGetResponse> findByOwner_IdAndEndBeforeAndStatus(long id, LocalDateTime end);

    @Query("select b from Booking b where b.item.owner.id  = ?1 " +
            "and b.status = ru.practicum.shareit.booking.Status.APPROVED " +
            "and b.start > ?2 " +
            "order by b.start")
    List<BookingGetResponse> findByOwner_IdAndStatusAndStartAfterOrderByStartAsc(long id, LocalDateTime start);
}