package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;
    @Enumerated
    private Status status;
    private LocalDateTime start;
    @Column(name = "end_booking")
    private LocalDateTime end;

    public Booking(Item item, User booker, Status status, LocalDateTime start, LocalDateTime end) {
        this.item = item;
        this.booker = booker;
        this.status = status;
        this.start = start;
        this.end = end;
    }
}
