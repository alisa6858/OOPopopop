package ru.lab5.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lab5.framework.model.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * <p>
 * Позволяет искать пользователей по идентификатору,
 * имени и роли. Наследуется от {@link JpaRepository},
 * поэтому включает базовые методы save, findAll, delete и другие.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Найти пользователя по имени.
     *
     * @param username логин пользователя
     * @return найденный пользователь или пустой результат
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Найти всех пользователей с указанной ролью.
     *
     * @param role код роли (0 — обычный, 1 — администратор)
     * @return список пользователей
     */
    List<UserEntity> findByRole(int role);
}
