package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.user.User;

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
    private List<Comment> comments;

    public Item(String name, String description, boolean available, User owner, List<Comment> comments) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

}
