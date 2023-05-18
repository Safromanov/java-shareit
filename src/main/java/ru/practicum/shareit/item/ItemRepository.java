package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i " +
            "where ((upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%'))))" +
            "and i.available = true")
    List<Item> findByNameOrDescription(String str);

    @Query("select i from Item i where i.id = ?1 and i.owner.id = ?2")
    Optional<Item> findByIdAndOwnerId(long idItem, long idOwner);

//    @Query("select i from Item i where i.id = ?1")
//    Optional<Item> findById(long idItem);

    @Query("select i from Item i where i.owner.id = ?1")
    List<Item> getAllByOwnerId(long idOwner);

}
