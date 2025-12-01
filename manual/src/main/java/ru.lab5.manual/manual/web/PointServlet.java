package ru.lab5.manual.web;

import ru.lab5.api.PointDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Сервлет для работы с точками внутри функций.
 * Операции:
 *  - GET  /api/points?id=...         – получить одну точку;
 *  - POST /api/points                – создать точку;
 *  - GET  /api/points?functionId=... – получить точки по функции.
 */
@WebServlet(name = "PointServlet", urlPatterns = "/api/points")
public class PointServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String idParam = req.getParameter("id");
        String functionIdParam = req.getParameter("functionId");

        PointDto point = new PointDto();

        if (idParam != null) {
            point.setId(Long.parseLong(idParam));
        } else {
            point.setId(1L);
        }

        if (functionIdParam != null) {
            point.setFunctionId(Long.parseLong(functionIdParam));
        } else {
            point.setFunctionId(100L);
        }

        point.setName("Демо-точка");
        point.setDescription("Описание демо-точки");

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"id\": %d," +
                            "\"functionId\": %d," +
                            "\"name\": \"%s\"," +
                            "\"description\": \"%s\"" +
                            "}",
                    point.getId(),
                    point.getFunctionId(),
                    point.getName(),
                    point.getDescription()
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String functionIdParam = req.getParameter("functionId");
        String name = req.getParameter("name");
        String description = req.getParameter("description");

        Long functionId = null;
        if (functionIdParam != null && !functionIdParam.isBlank()) {
            functionId = Long.parseLong(functionIdParam);
        }

        PointDto created = new PointDto(
                1L,
                functionId,
                name,
                description
        );

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"status\": \"created\"," +
                            "\"id\": %d," +
                            "\"functionId\": %d" +
                            "}",
                    created.getId(),
                    created.getFunctionId()
            );
        }
    }
}
