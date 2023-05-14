package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByNameLikeOrDescriptionLikeAllIgnoreCase(String name, String description);
    @Query("select i from Item i where upper(i.name) like upper(?1)")
    List<Item> findByNameLikeIgnoreCase(String name);

    Item findByNameIgnoreCaseAndDescriptionIgnoreCase(String name, String description);

//    @Query("select i from Item i where upper(i.name) = upper(?1) or upper(i.name) = upper(?2)")
//    List<Item> findByNameIgnoreCaseOrNameIgnoreCase(String name, String name1);



    Optional<Item> findByIdAndOwner_Id(long idItem, long idOwner);

    List<Item> getAllByOwner_Id(long idOwner);
}
