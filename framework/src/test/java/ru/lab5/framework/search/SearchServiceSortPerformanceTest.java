package ru.lab5.framework.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.lab5.framework.model.UserEntity;
import ru.lab5.framework.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Бенчмарк сортировки для SearchService в framework-варианте.
 *
 * Генерируется USERS_COUNT пользователей, после чего измеряется
 * время сортировки по имени и по роли.
 * Выводится строка:
 * framework_sort;USERS_COUNT;username_ms;role_ms
 */
@DataJpaTest
@Import(SearchService.class)
class SearchServiceSortPerformanceTest {

    private static final int USERS_COUNT = 10_000;

    @Autowired
    private SearchService searchService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        Random random = new Random();
        List<UserEntity> batch = new ArrayList<>(USERS_COUNT);

        for (int i = 0; i < USERS_COUNT; i++) {
            UserEntity u = new UserEntity();
            u.setUsername("user_" + i + "_" + random.nextInt(100_000));
            u.setPassword("p_" + random.nextInt(1_000_000));
            u.setRole(random.nextBoolean() ? 0 : 1);
            batch.add(u);
        }

        userRepository.saveAll(batch);
    }

    @Test
    void measureSortPerformance() {
        long t1 = System.currentTimeMillis();
        searchService.searchUsers(null, null, SearchSortField.USERNAME, true);
        long t2 = System.currentTimeMillis();
        long usernameMs = t2 - t1;

        long t3 = System.currentTimeMillis();
        searchService.searchUsers(null, null, SearchSortField.ROLE, true);
        long t4 = System.currentTimeMillis();
        long roleMs = t4 - t3;

        System.out.printf("framework_sort;%d;%d;%d%n",
                USERS_COUNT, usernameMs, roleMs);
    }
}
