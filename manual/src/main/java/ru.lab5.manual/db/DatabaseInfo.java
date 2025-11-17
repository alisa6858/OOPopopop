package ru.lab5.manual.db;

/**
 * Класс с описанием используемой системы управления базами данных.
 * <p>
 * В лабораторной работе в качестве основной реляционной базы данных
 * используется PostgreSQL. Данный класс фиксирует выбор СУБД и
 * содержит типичные параметры подключения, которые применяются
 * в модуле manual при работе через JDBC.
 */
public class DatabaseInfo {

    /**
     * Имя используемой СУБД.
     */
    public static final String DBMS_NAME = "PostgreSQL";

    /**
     * Рекомендуемый драйвер JDBC для PostgreSQL.
     */
    public static final String JDBC_DRIVER = "org.postgresql.Driver";

    /**
     * Пример URL подключения к базе данных.
     * <p>
     * В реальном проекте параметры хранятся во внешнем файле
     * настроек, а здесь строка служит для наглядности и
     * пояснения выбора.
     */
    public static final String SAMPLE_URL =
            "jdbc:postgresql://localhost:5432/tabulated_functions";

    private DatabaseInfo() {
        // утилитарный класс, создавать объект не требуется
    }
}
