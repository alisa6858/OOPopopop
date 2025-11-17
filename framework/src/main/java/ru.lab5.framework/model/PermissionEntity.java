package ru.lab5.framework.model;

import jakarta.persistence.*;

/**
 * Сущность разрешения на доступ к функции.
 * <p>
 * Соответствует таблице {@code permission}. Описывает,
 * какие права имеет пользователь на чужую функцию.
 */
@Entity
@Table(name = "permission")
public class PermissionEntity {

    /**
     * Первичный ключ разрешения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    /**
     * Пользователь, который получает доступ.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /**
     * Функция, к которой выдаётся доступ.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function_id", nullable = false)
    private FunctionEntity function;

    /**
     * Тип доступа:
     * 0 = NONE, 1 = READ, 2 = WRITE, 4 = DELETE.
     */
    @Column(name = "access", nullable = false)
    private int access;

    public PermissionEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public FunctionEntity getFunction() {
        return function;
    }

    public void setFunction(FunctionEntity function) {
        this.function = function;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }
}
