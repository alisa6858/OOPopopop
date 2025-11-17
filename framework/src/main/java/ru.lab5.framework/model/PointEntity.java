package ru.lab5.framework.model;

import jakarta.persistence.*;

/**
 * Сущность точки табулированной функции.
 * <p>
 * Соответствует таблице {@code point}. Точка связана
 * с одной функцией и имеет индекс внутри функции.
 */
@Entity
@Table(name = "point")
public class PointEntity {

    /**
     * Первичный ключ точки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    /**
     * Функция, к которой относится точка.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function_id", nullable = false)
    private FunctionEntity function;

    /**
     * Индекс точки внутри функции.
     * Уникален в паре (function_id, idx).
     */
    @Column(name = "idx", nullable = false)
    private int index;

    /**
     * Координата X.
     */
    @Column(name = "x", nullable = false)
    private double x;

    /**
     * Координата Y.
     */
    @Column(name = "y", nullable = false)
    private double y;

    public PointEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FunctionEntity getFunction() {
        return function;
    }

    public void setFunction(FunctionEntity function) {
        this.function = function;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
