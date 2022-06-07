package ua.university.utils;

import ua.university.exceptions.BadRequestException;

public class ServerUtils {
    private ServerUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static int parseParameterId(String id) {
        int intId;
        try {
            intId = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("Bad id parameter!");
        }
        return intId;
    }
}
