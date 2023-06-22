package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Optional<Item> findByIdAndOwnerId(long idItem, long idOwner);

    Page<Item> findByOwnerId(long idOwner, Pageable pageable);
}
