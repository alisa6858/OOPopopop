package ru.lab5.manual.dto;

/**
 * DTO для прав доступа к функции.
 */
public class PermissionDto {

    private Long id;
    private Long userId;
    private Long functionId;
    private int access;

    public PermissionDto() {
    }

    public PermissionDto(Long id, Long userId, Long functionId, int access) {
        this.id = id;
        this.userId = userId;
        this.functionId = functionId;
        this.access = access;
    }

    public Long getId() {
        return id;
    }

    public PermissionDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public PermissionDto setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public PermissionDto setFunctionId(Long functionId) {
        this.functionId = functionId;
        return this;
    }

    public int getAccess() {
        return access;
    }

    public PermissionDto setAccess(int access) {
        this.access = access;
        return this;
    }

    @Override
    public String toString() {
        return "PermissionDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", functionId=" + functionId +
                ", access=" + access +
                '}';
    }
}
