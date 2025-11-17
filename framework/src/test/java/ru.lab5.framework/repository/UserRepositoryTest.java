package ru.lab5.framework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.lab5.framework.model.UserEntity;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест репозитория пользователей.
 * <p>
 * Генерируются разные пользователи, затем проверяется
 * поиск по имени, роли и удаление записей.
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final Random random = new Random();

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void testGenerateFindAndDeleteUsers() {
        // создаём несколько пользователей с разными ролями
        UserEntity admin = new UserEntity();
        admin.setUsername("admin_" + random.nextInt(10_000));
        admin.setPassword("pass_" + random.nextInt(10_000));
        admin.setRole(1);

        UserEntity user1 = new UserEntity();
        user1.setUsername("user_" + random.nextInt(10_000));
        user1.setPassword("pass_" + random.nextInt(10_000));
        user1.setRole(0);

        UserEntity user2 = new UserEntity();
        user2.setUsername("user_" + random.nextInt(10_000));
        user2.setPassword("pass_" + random.nextInt(10_000));
        user2.setRole(0);

        admin = userRepository.save(admin);
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        assertNotNull(admin.getId());
        assertNotNull(user1.getId());
        assertNotNull(user2.getId());

        // поиск по имени
        var foundAdmin = userRepository.findByUsername(admin.getUsername());
        assertTrue(foundAdmin.isPresent());
        assertEquals(1, foundAdmin.get().getRole());

        // поиск по роли
        List<UserEntity> simpleUsers = userRepository.findByRole(0);
        assertEquals(2, simpleUsers.size());

        // удаление одного пользователя
        userRepository.delete(user1);
        List<UserEntity> afterDelete = userRepository.findByRole(0);
        assertEquals(1, afterDelete.size());

        // полная очистка
        userRepository.deleteAll();
        assertEquals(0, userRepository.count());
    }
}
