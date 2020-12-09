package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static boolean isBetweenHalfOpen(LocalDateTime value, LocalDateTime startDay, LocalDateTime endDay, LocalDateTime startTime, LocalDateTime endTime) {
        if (startDay == null) {
            startDay = LocalDateTime.MIN;
        }
        if (endDay == null) {
            endDay = LocalDateTime.MAX;
        }

        if (startTime == null) {
            startTime = LocalDateTime.MIN;
        }
        if (endTime == null) {
            endTime = LocalDateTime.MAX;
        }

        return (value.toLocalDate().compareTo(startDay.toLocalDate()) >= 0 && value.toLocalDate().compareTo(endDay.toLocalDate()) <= 0)
                &
                (value.toLocalTime().compareTo(startTime.toLocalTime()) >= 0 && value.toLocalTime().compareTo(endTime.toLocalTime()) < 0);
    }

    public static LocalDateTime getDateFomString(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return str.isEmpty() ? null : LocalDate.parse(str, formatter).atStartOfDay();
    }

    public static LocalDateTime getTimeFomString(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return str.isEmpty() ? null : LocalTime.parse(str, formatter).atDate(LocalDate.MAX);
    }

}

