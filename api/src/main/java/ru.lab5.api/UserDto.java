package ru.lab5.api;

import java.util.List;

/**
 * Короткое описание пользователя.
 * Этот класс используется и в manual-реализации, и в framework-реализации.
 */
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private Integer role;
    private List<PermissionDto> permissions;

    public UserDto() {
    }

    public UserDto(Long id, String username, String password, Integer role, List<PermissionDto> permissions) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.permissions = permissions;
    }

    /**
     * Идентификатор пользователя в базе.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Логин пользователя.
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Пароль в том виде, который хранится в БД.
     * В боевых системах здесь обычно хранится хэш.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Роль пользователя.
     * В пятой лабе ты уже использовал целочисленное поле.
     */
    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    /**
     * Список прав доступа пользователя к функциям.
     * Можно не заполнять при каждом запросе, если не нужно.
     */
    public List<PermissionDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDto> permissions) {
        this.permissions = permissions;
    }
}
