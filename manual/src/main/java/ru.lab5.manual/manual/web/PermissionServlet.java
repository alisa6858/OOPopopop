package ru.lab5.manual.web;

import ru.lab5.api.PermissionDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Сервлет для работы с правами доступа пользователей.
 * Операции:
 *  - POST   /api/permissions          – выдать право;
 *  - DELETE /api/permissions?id=...   – отозвать право;
 *  - GET    /api/permissions?userId=… – получить права пользователя.
 */
@WebServlet(name = "PermissionServlet", urlPatterns = "/api/permissions")
public class PermissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String userIdParam = req.getParameter("userId");

        // Заглушка: возвращаем одно право доступа.
        PermissionDto permission = new PermissionDto();
        permission.setId(1L);
        permission.setUserId(userIdParam != null ? Long.parseLong(userIdParam) : 1L);
        permission.setFunctionId(100L);
        permission.setAllowed(true);

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"id\": %d," +
                            "\"userId\": %d," +
                            "\"functionId\": %d," +
                            "\"allowed\": %s" +
                            "}",
                    permission.getId(),
                    permission.getUserId(),
                    permission.getFunctionId(),
                    permission.isAllowed() ? "true" : "false"
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String userIdParam = req.getParameter("userId");
        String functionIdParam = req.getParameter("functionId");
        String allowedParam = req.getParameter("allowed");

        Long userId = userIdParam != null ? Long.parseLong(userIdParam) : null;
        Long functionId = functionIdParam != null ? Long.parseLong(functionIdParam) : null;
        boolean allowed = "true".equalsIgnoreCase(allowedParam);

        PermissionDto created = new PermissionDto(
                1L,
                userId,
                functionId,
                allowed
        );

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"status\": \"created\"," +
                            "\"id\": %d" +
                            "}",
                    created.getId()
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
            // Здесь должен быть вызов DAO для удаления права доступа.
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
