package ru.lab5.manual.model;

/**
 * Модель строки таблицы app_user для ветки manual.
 */
public class UserRecord {

    private Long id;
    private String username;
    private String password;
    private int role;

    public UserRecord() {
    }

    public UserRecord(Long id, String username, String password, int role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserRecord(String username, String password, int role) {
        this(null, username, password, role);
    }

    public Long getId() {
        return id;
    }

    public UserRecord setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserRecord setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRecord setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getRole() {
        return role;
    }

    public UserRecord setRole(int role) {
        this.role = role;
        return this;
    }
}
