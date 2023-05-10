package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import ru.practicum.shareit.item.valid.UniqueLogin;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @UniqueLogin
    private String name;
    @NotBlank
    private String description;

    private boolean available;

}
