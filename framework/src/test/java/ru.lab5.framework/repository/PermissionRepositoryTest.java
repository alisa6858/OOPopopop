package ru.lab5.framework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.lab5.framework.model.FunctionEntity;
import ru.lab5.framework.model.PermissionEntity;
import ru.lab5.framework.model.UserEntity;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест репозитория прав доступа к функциям.
 */
@DataJpaTest
class PermissionRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    private final Random random = new Random();

    @BeforeEach
    void clean() {
        permissionRepository.deleteAll();
        functionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGenerateFindAndDeletePermissions() {
        // владелец и функция
        UserEntity owner = new UserEntity();
        owner.setUsername("owner_" + random.nextInt(10_000));
        owner.setPassword("pass_" + random.nextInt(10_000));
        owner.setRole(1);
        owner = userRepository.save(owner);

        FunctionEntity function = new FunctionEntity();
        function.setOwner(owner);
        function.setName("g(x)");
        function.setDescription("функция для прав");
        function = functionRepository.save(function);

        // пользователь, которому выдаём права
        UserEntity guest = new UserEntity();
        guest.setUsername("guest_" + random.nextInt(10_000));
        guest.setPassword("pass_" + random.nextInt(10_000));
        guest.setRole(0);
        guest = userRepository.save(guest);

        // создаём разрешение
        PermissionEntity perm = new PermissionEntity();
        perm.setUser(guest);
        perm.setFunction(function);
        perm.setAccess(1); // READ
        perm = permissionRepository.save(perm);

        assertNotNull(perm.getId());

        // поиск по id
        var fromDb = permissionRepository.findById(perm.getId());
        assertTrue(fromDb.isPresent());
        assertEquals(guest.getId(), fromDb.get().getUser().getId());

        // поиск по паре (user, function)
        var byPair = permissionRepository.findByUser_IdAndFunction_Id(guest.getId(), function.getId());
        assertTrue(byPair.isPresent());
        assertEquals(perm.getId(), byPair.get().getId());

        // поиск всех прав пользователя
        List<PermissionEntity> forGuest = permissionRepository.findByUser_Id(guest.getId());
        assertEquals(1, forGuest.size());

        // удаление одного разрешения
        permissionRepository.delete(perm);
        List<PermissionEntity> empty = permissionRepository.findByUser_Id(guest.getId());
        assertTrue(empty.isEmpty());
    }
}
