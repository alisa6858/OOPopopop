package ru.lab5.manual.web;

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
 * Примерно такие операции:
 *  - GET  /api/functions?id=...   – получить одну функцию;
 *  - POST /api/functions          – создать функцию;
 *  - DELETE /api/functions?id=... – удалить функцию.
 *
 * В реальной реализации здесь вызывается DAO/сервис из manual-части.
 */
@WebServlet(name = "FunctionServlet", urlPatterns = "/api/functions")
public class FunctionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String idParam = req.getParameter("id");

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
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String description = req.getParameter("description");

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
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String idParam = req.getParameter("id");
        boolean deleted = false;

        if (idParam != null) {
            long id = Long.parseLong(idParam);
            // Здесь нужно вызвать DAO для удаления функции.
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
