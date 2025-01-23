package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DEFAULT_FORMATTER);
    }
}
