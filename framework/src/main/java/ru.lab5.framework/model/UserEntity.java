package ru.lab5.framework.model;

import jakarta.persistence.*;

/**
 * Сущность пользователя системы.
 * <p>
 * Соответствует записи в таблице {@code app_user}.
 * Пользователь может владеть несколькими функциями и иметь
 * разные права доступа к чужим функциям.
 */
@Entity
@Table(name = "app_user")
public class UserEntity {

    /**
     * Первичный ключ пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * Уникальное имя пользователя (логин).
     */
    @Column(name = "username", length = 32, nullable = false, unique = true)
    private String username;

    /**
     * Хеш пароля пользователя.
     */
    @Column(name = "password", length = 128, nullable = false)
    private String password;

    /**
     * Роль пользователя.
     * 0 — обычный пользователь, 1 — администратор.
     */
    @Column(name = "role", nullable = false)
    private int role;

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
