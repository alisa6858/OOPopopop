package ru.lab5.manual.model;

/**
 * Модель строки таблицы app_function для ветки manual.
 */
public class FunctionRecord {

    private Long id;
    private Long userId;
    private String name;
    private String description;

    public FunctionRecord() {
    }

    public FunctionRecord(Long id, Long userId, String name, String description) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
    }

    public FunctionRecord(Long userId, String name, String description) {
        this(null, userId, name, description);
    }

    public Long getId() {
        return id;
    }

    public FunctionRecord setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public FunctionRecord setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public FunctionRecord setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FunctionRecord setDescription(String description) {
        this.description = description;
        return this;
    }
}
