package ru.lab5.manual.model;

/**
 * Модель строки таблицы point для ветки manual.
 */
public class PointRecord {

    private Long id;
    private Long functionId;
    private int index;
    private double x;
    private double y;

    public PointRecord() {
    }

    public PointRecord(Long id, Long functionId, int index, double x, double y) {
        this.id = id;
        this.functionId = functionId;
        this.index = index;
        this.x = x;
        this.y = y;
    }

    public PointRecord(Long functionId, int index, double x, double y) {
        this(null, functionId, index, x, y);
    }

    public Long getId() {
        return id;
    }

    public PointRecord setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public PointRecord setFunctionId(Long functionId) {
        this.functionId = functionId;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public PointRecord setIndex(int index) {
        this.index = index;
        return this;
    }

    public double getX() {
        return x;
    }

    public PointRecord setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public PointRecord setY(double y) {
        this.y = y;
        return this;
    }
}
