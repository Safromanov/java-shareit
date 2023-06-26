package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "bookings")
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
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private Status status;
    @Column(name = "booking_start")
    private LocalDateTime start;
    @Column(name = "booking_end")
    private LocalDateTime end;

    public Booking(Item item, User booker, Status status, LocalDateTime start, LocalDateTime end) {
        this.item = item;
        this.booker = booker;
        this.status = status;
        this.start = start;
        this.end = end;
    }
}
