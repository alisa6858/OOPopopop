package ru.lab5.framework.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Access Control API (framework)",
                version = "1.0",
                description = "REST-API для лабораторных 5–6, ветка framework"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Локальный сервер")
        }
)
public class OpenApiConfig {
    // Тело пустое, все задаём через аннотации
}
