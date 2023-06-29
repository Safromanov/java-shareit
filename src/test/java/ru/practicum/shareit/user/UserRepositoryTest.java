package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        User user = new User("name", "sin@ge.r");
        userRepository.save(user);
        User user2 = new User("name", "sun@ge.r");
        userRepository.save(user2);
        User userByEmail = userRepository.findByEmail(user2.getEmail()).get();
        assertEquals(2, userByEmail.getId());
        assertEquals(user2.getName(), userByEmail.getName());
    }
}
