package ru.lab5.manual.dto;

/**
 * DTO для табулированной функции.
 */
public class FunctionDto {

    private Long id;
    private Long ownerId;
    private String name;
    private String description;

    public FunctionDto() {
    }

    public FunctionDto(Long id, Long ownerId, String name, String description) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public FunctionDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public FunctionDto setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getName() {
        return name;
    }

    public FunctionDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FunctionDto setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return "FunctionDto{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
