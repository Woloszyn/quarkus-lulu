package co.woloszyn.common;

import jakarta.ws.rs.BadRequestException;

public class AssertionUtils {

    public static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new BadRequestException(message);
        }
    }

    public static void assertBoolean(boolean condition, boolean expected, String message) {
        if (condition != expected) {
            throw new BadRequestException(message);
        }
    }

    public static void assertStringNotEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new BadRequestException(message);
        }
    }

}