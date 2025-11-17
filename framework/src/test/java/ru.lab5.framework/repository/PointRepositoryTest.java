package ru.lab5.framework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.lab5.framework.model.FunctionEntity;
import ru.lab5.framework.model.PointEntity;
import ru.lab5.framework.model.UserEntity;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест репозитория точек табулированной функции.
 */
@DataJpaTest
class PointRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private PointRepository pointRepository;

    private final Random random = new Random();

    @BeforeEach
    void clean() {
        pointRepository.deleteAll();
        functionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGenerateFindAndDeletePoints() {
        // создаём пользователя и функцию
        UserEntity owner = new UserEntity();
        owner.setUsername("owner_" + random.nextInt(10_000));
        owner.setPassword("pass_" + random.nextInt(10_000));
        owner.setRole(0);
        owner = userRepository.save(owner);

        FunctionEntity function = new FunctionEntity();
        function.setOwner(owner);
        function.setName("f(x) = x^2");
        function.setDescription("функция для точек");
        function = functionRepository.save(function);

        // генерируем точки
        for (int i = 0; i < 5; i++) {
            PointEntity p = new PointEntity();
            p.setFunction(function);
            p.setIndex(i);
            double x = i;
            double y = x * x;
            p.setX(x);
            p.setY(y);
            pointRepository.save(p);
        }

        // поиск точек по функции
        List<PointEntity> points = pointRepository.findByFunction_IdOrderByIndex(function.getId());
        assertEquals(5, points.size());
        assertTrue(points.get(0).getIndex() < points.get(4).getIndex());

        // удаление одной точки
        PointEntity middle = points.get(2);
        pointRepository.delete(middle);

        List<PointEntity> afterDelete = pointRepository.findByFunction_IdOrderByIndex(function.getId());
        assertEquals(4, afterDelete.size());

        // полная очистка
        pointRepository.deleteAll();
        assertEquals(0, pointRepository.count());
    }
}
