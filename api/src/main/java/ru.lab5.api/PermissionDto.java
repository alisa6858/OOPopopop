package ru.lab5.api;

/**
 * Право доступа пользователя к функции.
 */
public class PermissionDto {

    private Long id;
    private Long userId;
    private Long functionId;
    private boolean allowed;

    public PermissionDto() {
    }

    public PermissionDto(Long id, Long userId, Long functionId, boolean allowed) {
        this.id = id;
        this.userId = userId;
        this.functionId = functionId;
        this.allowed = allowed;
    }

    /**
     * Идентификатор права доступа.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Идентификатор пользователя, которому выдано право.
     */
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Идентификатор функции, к которой относится право.
     */
    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    /**
     * Флаг, который показывает, разрешён доступ или нет.
     */
    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
}
