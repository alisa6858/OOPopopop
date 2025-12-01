package ru.lab5.api;

/**
 * Точка внутри функции.
 * В пятой лабе это была сущность, которая привязана к Function.
 */
public class PointDto {

    private Long id;
    private Long functionId;
    private String name;
    private String description;

    public PointDto() {
    }

    public PointDto(Long id, Long functionId, String name, String description) {
        this.id = id;
        this.functionId = functionId;
        this.name = name;
        this.description = description;
    }

    /**
     * Идентификатор точки.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Идентификатор функции, к которой относится точка.
     */
    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    /**
     * Название точки.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Дополнительное описание точки.
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
