package ru.lab5.manual.model;

/**
 * Модель строки таблицы permission для ветки manual.
 */
public class PermissionRecord {

    private Long id;
    private Long userId;
    private Long functionId;
    private int access;

    public PermissionRecord() {
    }

    public PermissionRecord(Long id, Long userId, Long functionId, int access) {
        this.id = id;
        this.userId = userId;
        this.functionId = functionId;
        this.access = access;
    }

    public PermissionRecord(Long userId, Long functionId, int access) {
        this(null, userId, functionId, access);
    }

    public Long getId() {
        return id;
    }

    public PermissionRecord setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public PermissionRecord setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public PermissionRecord setFunctionId(Long functionId) {
        this.functionId = functionId;
        return this;
    }

    public int getAccess() {
        return access;
    }

    public PermissionRecord setAccess(int access) {
        this.access = access;
        return this;
    }
}
