package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
//    @Query("select i from Item i where upper(i.name) like upper(?1)")
//    List<Item> findByNameLikeIgnoreCase(String name);
    long countByName(String login);
}
