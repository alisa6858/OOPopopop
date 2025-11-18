package ru.lab5.manual.dto;

/**
 * DTO-представление пользователя.
 * Используется для передачи данных во внешние слои
 * без пароля.
 */
public class UserDto {

    private Long id;
    private String username;
    private int role;

    public UserDto() {
    }

    public UserDto(Long id, String username, int role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public UserDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public int getRole() {
        return role;
    }

    public UserDto setRole(int role) {
        this.role = role;
        return this;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
