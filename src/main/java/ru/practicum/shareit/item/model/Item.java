package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
public class Item {
    @Id
    private long id;
    @NotBlank
    private String name;

    private String description;

    private boolean available;

}
