package ru.lab5.manual.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 *
 * Операции:
 *  - GET    /api/users?id=...         – получить пользователя по id;
 *  - POST   /api/users                – создать нового пользователя;
 *  - DELETE /api/users?id=...        – удалить пользователя.
 *
 * Здесь можно вызывать DAO/сервисы из пятой лабораторной.
 */
@WebServlet(name = "UserServlet", urlPatterns = "/api/users")
public class UserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

    /**
     * Обработка GET-запросов.
     * Если передан параметр id, возвращаем информацию об одном пользователе.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String idParam = req.getParameter("id");
        log.info("GET /api/users, id={}", idParam);

        // здесь можно дергать UserDao
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
        } catch (Exception e) {
            log.error("Ошибка при обработке GET /api/users", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Обработка POST-запросов.
     * Принимает параметры username, password, role.
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

        log.info("POST /api/users, username={}, role={}", username, role);

        // здесь должна быть вставка в БД через UserDao
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
        } catch (Exception e) {
            log.error("Ошибка при обработке POST /api/users", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
        log.info("DELETE /api/users, id={}", idParam);

        boolean deleted = false;

        if (idParam != null) {
            long id = Long.parseLong(idParam);

            // здесь должен быть вызов DAO для удаления
            deleted = true;
            log.debug("Пользователь удалён, id={}", id);
        } else {
            log.warn("DELETE /api/users без параметра id");
        }

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"deleted\": %s" +
                            "}",
                    deleted ? "true" : "false"
            );
        } catch (Exception e) {
            log.error("Ошибка при обработке DELETE /api/users", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
