package ru.lab5.manual.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Простые утилиты для работы с Base64.
 */
public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static String base64Decode(String value) {
        byte[] decoded = Base64.getDecoder().decode(value);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    public static String base64Encode(String value) {
        byte[] encoded = Base64.getEncoder().encode(value.getBytes(StandardCharsets.UTF_8));
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
