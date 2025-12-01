package ru.lab5.api;

/**
 * Параметры поиска пользователей.
 * Позволяет описать фильтрацию, сортировку и постраничную выборку.
 */
public class UserSearchRequest {

    private String usernameLike;
    private Integer roleEquals;
    private SortField sortField;
    private SortDirection sortDirection;
    private PageRequest page;

    public UserSearchRequest() {
    }

    public String getUsernameLike() {
        return usernameLike;
    }

    public void setUsernameLike(String usernameLike) {
        this.usernameLike = usernameLike;
    }

    public Integer getRoleEquals() {
        return roleEquals;
    }

    public void setRoleEquals(Integer roleEquals) {
        this.roleEquals = roleEquals;
    }

    public SortField getSortField() {
        return sortField;
    }

    public void setSortField(SortField sortField) {
        this.sortField = sortField;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public PageRequest getPage() {
        return page;
    }

    public void setPage(PageRequest page) {
        this.page = page;
    }
}
