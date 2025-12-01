package ru.lab5.manual.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab5.api.FunctionDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Сервлет для работы с функциями.
 *
 * Операции:
 *  - GET    /api/functions?id=...   – получить одну функцию;
 *  - POST   /api/functions          – создать функцию;
 *  - DELETE /api/functions?id=...   – удалить функцию.
 *
 * Здесь можно вызывать DAO/сервис из manual-части (FunctionDao и т.п.).
 */
@WebServlet(name = "FunctionServlet", urlPatterns = "/api/functions")
public class FunctionServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(FunctionServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        String idParam = req.getParameter("id");

        log.info("GET /api/functions, id={}", idParam);

        // тут будет поиск в БД по id
        FunctionDto function = new FunctionDto();
        function.setId(idParam != null ? Long.parseLong(idParam) : 1L);
        function.setCode("F-001");
        function.setName("Демо-функция");
        function.setDescription("Описание демо-функции");

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"id\": %d," +
                            "\"code\": \"%s\"," +
                            "\"name\": \"%s\"," +
                            "\"description\": \"%s\"" +
                            "}",
                    function.getId(),
                    function.getCode(),
                    function.getName(),
                    function.getDescription()
            );
        } catch (Exception e) {
            log.error("Ошибка при обработке GET /api/functions", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String description = req.getParameter("description");

        log.info("POST /api/functions, code={}, name={}", code, name);

        // здесь нужно вызвать DAO/сервис для сохранения функции
        FunctionDto created = new FunctionDto(
                1L,
                code,
                name,
                description
        );

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"status\": \"created\"," +
                            "\"id\": %d," +
                            "\"code\": \"%s\"" +
                            "}",
                    created.getId(),
                    created.getCode()
            );
        } catch (Exception e) {
            log.error("Ошибка при обработке POST /api/functions", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String idParam = req.getParameter("id");
        log.info("DELETE /api/functions, id={}", idParam);

        boolean deleted = false;

        if (idParam != null) {
            long id = Long.parseLong(idParam);
            // вызов DAO для удаления функции по id
            deleted = true;
            log.debug("Функция помечена как удалённая, id={}", id);
        } else {
            log.warn("DELETE /api/functions без параметра id");
        }

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"deleted\": %s" +
                            "}",
                    deleted ? "true" : "false"
            );
        } catch (Exception e) {
            log.error("Ошибка при обработке DELETE /api/functions", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
