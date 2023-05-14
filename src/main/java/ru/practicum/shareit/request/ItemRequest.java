package ru.practicum.shareit.request;

/**
 * TODO Sprint add-item-requests.
 */

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;
    private LocalDateTime created;

}
