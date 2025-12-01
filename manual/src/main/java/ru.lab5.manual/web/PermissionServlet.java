package ru.lab5.manual.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 *
 * Операции:
 *  - POST   /api/permissions            – выдать право;
 *  - DELETE /api/permissions?id=...     – отозвать право;
 *  - GET    /api/permissions?userId=…   – получить права пользователя.
 *
 * Здесь можно дергать PermissionDao или общий сервис доступа.
 */
@WebServlet(name = "PermissionServlet", urlPatterns = "/api/permissions")
public class PermissionServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(PermissionServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String userIdParam = req.getParameter("userId");
        log.info("GET /api/permissions, userId={}", userIdParam);

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
        } catch (Exception e) {
            log.error("Ошибка при обработке GET /api/permissions", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

        log.info("POST /api/permissions, userId={}, functionId={}, allowed={}",
                userId, functionId, allowed);

        // здесь запись права в БД через PermissionDao
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
        } catch (Exception e) {
            log.error("Ошибка при обработке POST /api/permissions", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String idParam = req.getParameter("id");
        log.info("DELETE /api/permissions, id={}", idParam);

        boolean deleted = false;

        if (idParam != null) {
            long id = Long.parseLong(idParam);
            // вызов DAO для удаления права доступа
            deleted = true;
            log.debug("Право доступа удалено, id={}", id);
        } else {
            log.warn("DELETE /api/permissions без параметра id");
        }

        try (PrintWriter writer = resp.getWriter()) {
            writer.printf(
                    "{" +
                            "\"deleted\": %s" +
                            "}",
                    deleted ? "true" : "false"
            );
        } catch (Exception e) {
            log.error("Ошибка при обработке DELETE /api/permissions", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
