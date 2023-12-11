package com.parallel;

import java.time.Duration;
import java.time.LocalDateTime;

public class Logger {

    static LocalDateTime baseDateTime = LocalDateTime.now();

    public static void log(String message) {
        LocalDateTime currDateTime = LocalDateTime.now();
        Duration duration = Duration.between(baseDateTime, currDateTime);

        // Total seconds since the last reset
        long totalSeconds = duration.getSeconds();

        // Calculate hours and minutes
        long hours = 9 + totalSeconds / 60;
        long minutes = totalSeconds % 60;

        // Displaying as HH:XX
        System.out.printf("%02d:%02d | %s %n", hours, minutes, message);
    }

    public static void reset() {
        baseDateTime = LocalDateTime.now();
    }
}
