package ru.lab5.manual.dto;

/**
 * DTO для точки табулированной функции.
 */
public class PointDto {

    private Long id;
    private Long functionId;
    private int index;
    private double x;
    private double y;

    public PointDto() {
    }

    public PointDto(Long id, Long functionId, int index, double x, double y) {
        this.id = id;
        this.functionId = functionId;
        this.index = index;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public PointDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public PointDto setFunctionId(Long functionId) {
        this.functionId = functionId;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public PointDto setIndex(int index) {
        this.index = index;
        return this;
    }

    public double getX() {
        return x;
    }

    public PointDto setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public PointDto setY(double y) {
        this.y = y;
        return this;
    }

    @Override
    public String toString() {
        return "PointDto{" +
                "id=" + id +
                ", functionId=" + functionId +
                ", index=" + index +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
