package ru.lab5.api;

/**
 * Описание функции (операции, экрана, действия),
 * к которой можно выдать доступ пользователю.
 */
public class FunctionDto {

    private Long id;
    private String code;
    private String name;
    private String description;

    public FunctionDto() {
    }

    public FunctionDto(Long id, String code, String name, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    /**
     * Идентификатор функции.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Краткий код функции, который удобно использовать в логике.
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Человекочитаемое название функции.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Любое текстовое описание, можно оставлять пустым.
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
