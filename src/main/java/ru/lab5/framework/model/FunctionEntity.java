package ru.lab5.framework.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Сущность табулированной функции.
 * <p>
 * Соответствует таблице {@code app_function}. Функция принадлежит
 * одному пользователю и содержит набор точек.
 */
@Entity
@Table(name = "app_function")
public class FunctionEntity {

    /**
     * Первичный ключ функции.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "function_id")
    private Long id;

    /**
     * Владелец функции.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

    /**
     * Название функции.
     */
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    /**
     * Описание функции, может быть пустым.
     */
    @Column(name = "description")
    private String description;

    /**
     * Список точек функции.
     * <p>
     * Связь не обязательна для работы сущности, но удобна
     * при загрузке функции вместе с её точками.
     */
    @OneToMany(mappedBy = "function", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointEntity> points;

    public FunctionEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PointEntity> getPoints() {
        return points;
    }

    public void setPoints(List<PointEntity> points) {
        this.points = points;
    }
}
