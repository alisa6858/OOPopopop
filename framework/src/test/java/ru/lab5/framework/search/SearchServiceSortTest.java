package ru.lab5.framework.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.lab5.framework.model.UserEntity;
import ru.lab5.framework.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест корректности сортировки в SearchService (framework-вариант).
 *
 * Создаётся несколько сущностей UserEntity, дальше проверяется
 * порядок в результатах вызова searchUsers.
 */
@DataJpaTest
@Import(SearchService.class)
class SearchServiceSortTest {

    @Autowired
    private SearchService searchService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        UserEntity u1 = new UserEntity();
        u1.setUsername("alex");
        u1.setPassword("p1");
        u1.setRole(0);

        UserEntity u2 = new UserEntity();
        u2.setUsername("boris");
        u2.setPassword("p2");
        u2.setRole(1);

        UserEntity u3 = new UserEntity();
        u3.setUsername("anna");
        u3.setPassword("p3");
        u3.setRole(0);

        UserEntity u4 = new UserEntity();
        u4.setUsername("dmitry");
        u4.setPassword("p4");
        u4.setRole(1);

        userRepository.saveAll(List.of(u1, u2, u3, u4));
    }

    @Test
    void testSortByUsernameAscending() {
        List<UserEntity> users = searchService.searchUsers(
                null,
                null,
                SearchSortField.USERNAME,
                true
        );

        assertEquals("alex", users.get(0).getUsername());
        assertEquals("anna", users.get(1).getUsername());
        assertEquals("boris", users.get(2).getUsername());
        assertEquals("dmitry", users.get(3).getUsername());
    }

    @Test
    void testSortByUsernameDescending() {
        List<UserEntity> users = searchService.searchUsers(
                null,
                null,
                SearchSortField.USERNAME,
                false
        );

        assertEquals("dmitry", users.get(0).getUsername());
        assertEquals("boris", users.get(1).getUsername());
        assertEquals("anna", users.get(2).getUsername());
        assertEquals("alex", users.get(3).getUsername());
    }

    @Test
    void testSortByRoleThenUsername() {
        List<UserEntity> users = searchService.searchUsers(
                null,
                null,
                SearchSortField.ROLE,
                true
        );

        assertEquals(0, users.get(0).getRole());
        assertEquals(0, users.get(1).getRole());
        assertEquals(1, users.get(2).getRole());
        assertEquals(1, users.get(3).getRole());

        assertEquals("alex", users.get(0).getUsername());
        assertEquals("anna", users.get(1).getUsername());
    }
}
