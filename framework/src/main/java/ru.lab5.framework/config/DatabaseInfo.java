package ru.lab5.framework.config;

/**
 * Описание выбранной реляционной базы данных
 * для модуля framework, в котором используется Spring Data JPA.
 * <p>
 * В качестве основной СУБД выбрана PostgreSQL. Класс содержит
 * базовую информацию, которая может использоваться в логах,
 * тестах и служить пояснением при защите работы.
 */
public class DatabaseInfo {

    /**
     * Имя СУБД, с которой работает приложение.
     */
    public static final String DBMS_NAME = "PostgreSQL";

    /**
     * Стандартный порт PostgreSQL.
     */
    public static final int DEFAULT_PORT = 5432;

    /**
     * Пример имени базы данных для лабораторной работы.
     */
    public static final String DEFAULT_DATABASE_NAME = "tabulated_functions";

    private DatabaseInfo() {
        // конструктор скрыт, экземпляры не создаются
    }
}
