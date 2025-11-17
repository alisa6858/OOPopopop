package ru.lab5.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lab5.framework.model.PermissionEntity;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с правами доступа к функциям.
 */
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    /**
     * Найти разрешение по пользователю и функции.
     *
     * @param userId     идентификатор пользователя
     * @param functionId идентификатор функции
     * @return найденное разрешение или пустой результат
     */
    Optional<PermissionEntity> findByUser_IdAndFunction_Id(Long userId, Long functionId);

    /**
     * Найти все разрешения пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список разрешений
     */
    List<PermissionEntity> findByUser_Id(Long userId);
}
