package ru.lab5.framework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.lab5.framework.model.FunctionEntity;
import ru.lab5.framework.model.UserEntity;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест репозитория табулированных функций.
 */
@DataJpaTest
class FunctionRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    private final Random random = new Random();

    @BeforeEach
    void clean() {
        functionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGenerateFindAndDeleteFunctions() {
        // создаём пользователя
        UserEntity owner = new UserEntity();
        owner.setUsername("owner_" + random.nextInt(10_000));
        owner.setPassword("pass_" + random.nextInt(10_000));
        owner.setRole(0);
        owner = userRepository.save(owner);

        // создаём несколько функций
        FunctionEntity f1 = new FunctionEntity();
        f1.setOwner(owner);
        f1.setName("sin(x)");
        f1.setDescription("первая тестовая функция");

        FunctionEntity f2 = new FunctionEntity();
        f2.setOwner(owner);
        f2.setName("cos(x)");
        f2.setDescription("вторая тестовая функция");

        FunctionEntity f3 = new FunctionEntity();
        f3.setOwner(owner);
        f3.setName("exp(x)");
        f3.setDescription("третья тестовая функция");

        functionRepository.save(f1);
        functionRepository.save(f2);
        functionRepository.save(f3);

        // поиск по владельцу
        List<FunctionEntity> forOwner = functionRepository.findByOwner_Id(owner.getId());
        assertEquals(3, forOwner.size());

        // поиск по части названия
        List<FunctionEntity> trig = functionRepository.findByNameContainingIgnoreCase("x");
        assertEquals(3, trig.size());

        List<FunctionEntity> sinOnly = functionRepository.findByNameContainingIgnoreCase("sin");
        assertEquals(1, sinOnly.size());
        assertEquals("sin(x)", sinOnly.get(0).getName());

        // удаление одной функции
        functionRepository.delete(sinOnly.get(0));
        List<FunctionEntity> afterDelete = functionRepository.findByOwner_Id(owner.getId());
        assertEquals(2, afterDelete.size());

        // полная очистка
        functionRepository.deleteAll();
        assertEquals(0, functionRepository.count());
    }
}
