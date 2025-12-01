package ru.lab5.framework.service;

import org.springframework.stereotype.Service;
import ru.lab5.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Простая in-memory реализация контракта AccessControlApi.
 * Здесь данные хранятся в памяти, без подключения к базе.
 * Для лабораторной работы этого достаточно, чтобы показать
 * работу контроллеров и единого API.
 */
@Service
public class FrameworkAccessControlService implements AccessControlApi {

    private final ConcurrentHashMap<Long, UserDto> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, FunctionDto> functions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, PointDto> points = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, PermissionDto> permissions = new ConcurrentHashMap<>();

    private final AtomicLong userIdSeq = new AtomicLong(1);
    private final AtomicLong functionIdSeq = new AtomicLong(1);
    private final AtomicLong pointIdSeq = new AtomicLong(1);
    private final AtomicLong permissionIdSeq = new AtomicLong(1);

    // -------- Пользователи --------

    @Override
    public UserDto createUser(UserDto user) {
        long id = userIdSeq.getAndIncrement();
        UserDto stored = new UserDto(
                id,
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getPermissions()
        );
        users.put(id, stored);
        return stored;
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<UserDto> findUsers(UserSearchRequest request) {
        List<UserDto> result = new ArrayList<>(users.values());

        if (request != null) {
            if (request.getUsernameLike() != null && !request.getUsernameLike().isBlank()) {
                String pattern = request.getUsernameLike().toLowerCase();
                result.removeIf(u -> u.getUsername() == null
                        || !u.getUsername().toLowerCase().contains(pattern));
            }
            if (request.getRoleEquals() != null) {
                result.removeIf(u -> u.getRole() == null
                        || !request.getRoleEquals().equals(u.getRole()));
            }

            if (request.getSortField() != null) {
                result.sort((u1, u2) -> {
                    int cmp = 0;
                    switch (request.getSortField()) {
                        case USERNAME:
                            String n1 = u1.getUsername() != null ? u1.getUsername() : "";
                            String n2 = u2.getUsername() != null ? u2.getUsername() : "";
                            cmp = n1.compareToIgnoreCase(n2);
                            break;
                        case ROLE:
                            Integer r1 = u1.getRole() != null ? u1.getRole() : 0;
                            Integer r2 = u2.getRole() != null ? u2.getRole() : 0;
                            cmp = r1.compareTo(r2);
                            break;
                        default:
                            break;
                    }
                    if (request.getSortDirection() == SortDirection.DESC) {
                        cmp = -cmp;
                    }
                    return cmp;
                });
            }

            if (request.getPage() != null) {
                int page = Math.max(0, request.getPage().getPageNumber());
                int size = Math.max(1, request.getPage().getPageSize());
                int from = page * size;
                int to = Math.min(from + size, result.size());
                if (from < result.size()) {
                    result = result.subList(from, to);
                } else {
                    result = List.of();
                }
            }
        }

        return result;
    }

    @Override
    public Optional<UserDto> updateUser(Long id, UserDto user) {
        if (!users.containsKey(id)) {
            return Optional.empty();
        }
        UserDto updated = new UserDto(
                id,
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getPermissions()
        );
        users.put(id, updated);
        return Optional.of(updated);
    }

    @Override
    public boolean deleteUser(Long id) {
        return users.remove(id) != null;
    }

    // -------- Функции --------

    @Override
    public FunctionDto createFunction(FunctionDto function) {
        long id = functionIdSeq.getAndIncrement();
        FunctionDto stored = new FunctionDto(
                id,
                function.getCode(),
                function.getName(),
                function.getDescription()
        );
        functions.put(id, stored);
        return stored;
    }

    @Override
    public Optional<FunctionDto> getFunctionById(Long id) {
        return Optional.ofNullable(functions.get(id));
    }

    @Override
    public List<FunctionDto> getAllFunctions() {
        return new ArrayList<>(functions.values());
    }

    @Override
    public Optional<FunctionDto> updateFunction(Long id, FunctionDto function) {
        if (!functions.containsKey(id)) {
            return Optional.empty();
        }
        FunctionDto updated = new FunctionDto(
                id,
                function.getCode(),
                function.getName(),
                function.getDescription()
        );
        functions.put(id, updated);
        return Optional.of(updated);
    }

    @Override
    public boolean deleteFunction(Long id) {
        return functions.remove(id) != null;
    }

    // -------- Точки --------

    @Override
    public PointDto createPoint(PointDto point) {
        long id = pointIdSeq.getAndIncrement();
        PointDto stored = new PointDto(
                id,
                point.getFunctionId(),
                point.getName(),
                point.getDescription()
        );
        points.put(id, stored);
        return stored;
    }

    @Override
    public Optional<PointDto> getPointById(Long id) {
        return Optional.ofNullable(points.get(id));
    }

    @Override
    public List<PointDto> getPointsByFunctionId(Long functionId) {
        List<PointDto> result = new ArrayList<>();
        for (PointDto p : points.values()) {
            if (p.getFunctionId() != null && p.getFunctionId().equals(functionId)) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public Optional<PointDto> updatePoint(Long id, PointDto point) {
        if (!points.containsKey(id)) {
            return Optional.empty();
        }
        PointDto updated = new PointDto(
                id,
                point.getFunctionId(),
                point.getName(),
                point.getDescription()
        );
        points.put(id, updated);
        return Optional.of(updated);
    }

    @Override
    public boolean deletePoint(Long id) {
        return points.remove(id) != null;
    }

    // -------- Права доступа --------

    @Override
    public PermissionDto grantPermission(PermissionDto permission) {
        long id = permissionIdSeq.getAndIncrement();
        PermissionDto stored = new PermissionDto(
                id,
                permission.getUserId(),
                permission.getFunctionId(),
                permission.isAllowed()
        );
        permissions.put(id, stored);
        return stored;
    }

    @Override
    public boolean revokePermission(Long permissionId) {
        return permissions.remove(permissionId) != null;
    }

    @Override
    public List<PermissionDto> getPermissionsByUserId(Long userId) {
        List<PermissionDto> result = new ArrayList<>();
        for (PermissionDto p : permissions.values()) {
            if (p.getUserId() != null && p.getUserId().equals(userId)) {
                result.add(p);
            }
        }
        return result;
    }

    // -------- Обход структуры функций --------

    @Override
    public List<FunctionDto> traverseFunctionsDepthFirst(Long rootFunctionId) {
        // Для простоты вернём все функции.
        return getAllFunctions();
    }

    @Override
    public List<FunctionDto> traverseFunctionsBreadthFirst(Long rootFunctionId) {
        // Аналогично, простой вариант.
        return getAllFunctions();
    }

    @Override
    public List<FunctionDto> loadFunctionHierarchy(Long rootFunctionId) {
        // Пока без сложной иерархии.
        return getAllFunctions();
    }
}
