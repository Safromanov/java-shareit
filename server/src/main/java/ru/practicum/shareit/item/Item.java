package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "items")
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "item_request_id")
    private ItemRequest itemRequest;

    public Item(String name, String description, boolean available, User owner, List<Comment> comments, ItemRequest itemRequest) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.itemRequest = itemRequest;
    }
}
