package ru.lab5.api;

/**
 * Простое описание страницы для постраничного получения данных.
 * Используется в операциях поиска.
 */
public class PageRequest {

    private int pageNumber;
    private int pageSize;

    public PageRequest() {
    }

    public PageRequest(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * Номер страницы, начиная с нуля.
     */
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Размер страницы, сколько записей возвращать за один запрос.
     */
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
