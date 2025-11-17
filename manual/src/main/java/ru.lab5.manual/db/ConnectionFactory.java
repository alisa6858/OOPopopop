package ru.lab5.manual.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Простая фабрика подключений к базе данных PostgreSQL.
 * <p>
 * Параметры подключения берутся из файла
 * {@code db/postgres-connection.properties}, который лежит
 * в ресурсах модуля manual.
 */
public class ConnectionFactory {

    private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);

    private static final String PROPERTIES_PATH = "/db/postgres-connection.properties";

    private static String url;
    private static String username;
    private static String password;

    static {
        loadProperties();
        registerDriver();
    }

    private ConnectionFactory() {
    }

    private static void loadProperties() {
        Properties props = new Properties();
        try (InputStream in = ConnectionFactory.class.getResourceAsStream(PROPERTIES_PATH)) {
            if (in == null) {
                throw new IllegalStateException("Не найден файл настроек " + PROPERTIES_PATH);
            }
            props.load(in);
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            log.info("Настройки подключения к БД загружены, url={}", url);
        } catch (IOException e) {
            log.error("Ошибка при загрузке свойств подключения к БД", e);
            throw new RuntimeException("Не удалось загрузить настройки БД", e);
        }
    }

    private static void registerDriver() {
        try {
            Class.forName("org.postgresql.Driver");
            log.info("JDBC-драйвер PostgreSQL успешно зарегистрирован");
        } catch (ClassNotFoundException e) {
            log.error("Драйвер PostgreSQL не найден в classpath", e);
            throw new RuntimeException("Драйвер PostgreSQL не найден", e);
        }
    }

    /**
     * Получить новое подключение к базе данных.
     *
     * @return живое подключение {@link Connection}
     * @throws SQLException при ошибке соединения
     */
    public static Connection getConnection() throws SQLException {
        log.debug("Открытие нового подключения к БД");
        return DriverManager.getConnection(url, username, password);
    }
}
