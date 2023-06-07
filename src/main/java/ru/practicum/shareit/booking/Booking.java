package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    private LocalDateTime  start;
    @Column(name = "end_booking")
    private LocalDateTime end;
    @ManyToOne
    private Item item;
    @ManyToOne
    private User booker;
    private String status;
}
