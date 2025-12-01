package ru.lab5.manual.security;

import ru.lab5.manual.dto.UserDto;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Фильтр Basic Auth для manual-ветки.
 * Перехватывает запросы к /api/* и проверяет заголовок Authorization.
 */
@WebFilter(urlPatterns = "/api/*")
public class BasicAuthFilter implements Filter {

    private AuthService authService;

    @Override
    public void init(FilterConfig filterConfig) {
        this.authService = new AuthService();
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        // Можно дать свободный доступ к какому-то ping-методу,
        // но для простоты всё /api/* закрываем авторизацией.

        String authHeader = httpReq.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            sendUnauthorized(httpResp);
            return;
        }

        String base64 = authHeader.substring("Basic ".length()).trim();
        String decoded = SecurityUtil.base64Decode(base64);
        int colonIndex = decoded.indexOf(':');

        if (colonIndex <= 0) {
            sendUnauthorized(httpResp);
            return;
        }

        String username = decoded.substring(0, colonIndex);
        String password = decoded.substring(colonIndex + 1);

        Optional<UserDto> userOpt = authService.authenticate(username, password);
        if (userOpt.isEmpty()) {
            sendUnauthorized(httpResp);
            return;
        }

        // Кладём пользователя в атрибут запроса — сервлеты смогут взять роль, id и т.д.
        httpReq.setAttribute("authUser", userOpt.get());

        chain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setHeader("WWW-Authenticate", "Basic realm=\"lab5-manual\"");
        resp.getWriter().write("Unauthorized");
    }

    @Override
    public void destroy() {
    }
}
