package ru.lab5.framework.perf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.lab5.framework.model.UserEntity;
import ru.lab5.framework.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест для измерения времени работы Spring Data JPA репозитория.
 * <p>
 * Сценарий такой же, как в ManualUserDaoPerformanceTest:
 * 1) очистка таблицы пользователей;
 * 2) генерация 10 000 пользователей;
 * 3) вставка всех пользователей;
 * 4) чтение всех пользователей;
 * 5) удаление всех пользователей.
 * <p>
 * Время работы выводится в консоль в виде строки CSV:
 * framework;10000;insertMs;selectMs;deleteMs
 */
@DataJpaTest
class FrameworkUserRepositoryPerformanceTest {

    @Autowired
    private UserRepository userRepository;

    private final Random random = new Random();

    private static final int USERS_COUNT = 10_000;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void measureFrameworkUserRepositoryPerformance() {
        // 1. Генерация сущностей
        List<UserEntity> entities = new ArrayList<>(USERS_COUNT);
        for (int i = 0; i < USERS_COUNT; i++) {
            UserEntity user = new UserEntity();
            user.setUsername("framework_user_" + i + "_" + random.nextInt(100_000));
            user.setPassword("pass_" + random.nextInt(1_000_000));
            user.setRole(random.nextBoolean() ? 0 : 1);
            entities.add(user);
        }

        // 2. Вставка (saveAll)
        long tInsertStart = System.currentTimeMillis();
        userRepository.saveAll(entities);
        long tInsertEnd = System.currentTimeMillis();
        long insertMs = tInsertEnd - tInsertStart;

        // 3. Чтение всех записей
        long tSelectStart = System.currentTimeMillis();
        List<UserEntity> allUsers = userRepository.findAll();
        long tSelectEnd = System.currentTimeMillis();
        long selectMs = tSelectEnd - tSelectStart;

        assertEquals(USERS_COUNT, allUsers.size(),
                "Количество записей в app_user не совпадает с ожидаемым для framework");

        // 4. Удаление всех пользователей
        long tDeleteStart = System.currentTimeMillis();
        userRepository.deleteAll();
        long tDeleteEnd = System.currentTimeMillis();
        long deleteMs = tDeleteEnd - tDeleteStart;

        // 5. Вывод строки для таблички
        System.out.printf("framework;%d;%d;%d;%d%n",
                USERS_COUNT, insertMs, selectMs, deleteMs);
    }
}
