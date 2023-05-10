package ru.practicum.shareit.item.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import ru.practicum.shareit.item.service.ItemService;


public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {

    @Autowired
    private ItemService itemService;

//    @Override
//    public void initialize(UniqueLogin annotation) {
//        // Intentionally empty: nothing to initialize
//    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {

        if (value == null) {
            return true;
        }

        boolean loginExists = itemService.countByLogin(value) > 0;

        return !loginExists;
    }

}