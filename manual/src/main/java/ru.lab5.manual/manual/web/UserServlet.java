package ru.lab5.manual.web;

import ru.lab5.api.UserDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Сервлет для работы с пользователями на стороне manual-реализации.
 * Здесь будет простой пример обработки запросов:
 *  - GET  /api/users?id=...      – получить пользователя по id;
 *  - POST /api/users             – создать нового пользователя;
 *  - DELETE /api/users?id=...    – удалить пользователя.
 *
 * Внутри сервлета можно вызывать DAO или сервисы из пятой лабораторной.
 * Сейчас используется упрощённая заглушка, чтобы показать структуру.
 */
@WebServlet(name = "UserServlet", urlPatterns = "/api/users")
public class UserServlet extends HttpServlet {

    /**
     * Обработка GET-запросов.
     * Если передан параметр id, возвращаем информацию об одном пользователе.
     * В противном случае можно вернуть список, но здесь ограничимся заглушкой.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String idParam = req.getParameter("id");

        // Здесь можно вызвать реальный сервис/DAO.
        // Пока делаем простой пример, чтобы не привязываться к конкретным классам.
        UserDto userDto = new UserDto();
        userDto.setId(idParam != null ? Long.parseLong(idParam) : 1L);
        userDto.setUsername("demo-user");
        userDto.setPassword("secret");
        userDto.setRole(0);

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"id\": %d," +
                            "\"username\": \"%s\"," +
                            "\"password\": \"%s\"," +
                            "\"role\": %d" +
                            "}",
                    userDto.getId(),
                    userDto.getUsername(),
                    userDto.getPassword(),
                    userDto.getRole()
            );
        }
    }

    /**
     * Обработка POST-запросов.
     * Принимает параметры username, password, role.
     * В реальной реализации здесь нужно будет вызвать DAO для вставки в базу.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String roleParam = req.getParameter("role");

        Integer role = null;
        if (roleParam != null && !roleParam.isBlank()) {
            role = Integer.parseInt(roleParam);
        }

        // Здесь должна быть вставка в БД через DAO.
        UserDto created = new UserDto(
                1L,             // здесь потом можно подставить id из базы
                username,
                password,
                role,
                null
        );

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"status\": \"created\"," +
                            "\"id\": %d," +
                            "\"username\": \"%s\"" +
                            "}",
                    created.getId(),
                    created.getUsername()
            );
        }
    }

    /**
     * Обработка DELETE-запросов.
     * Ожидается параметр id.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String idParam = req.getParameter("id");
        boolean deleted = false;

        if (idParam != null) {
            long id = Long.parseLong(idParam);

            // Здесь должен быть вызов DAO для удаления пользователя.
            // Пока просто считаем, что удаление прошло успешно.
            deleted = true;
        }

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"deleted\": %s" +
                            "}",
                    deleted ? "true" : "false"
            );
        }
    }
}
