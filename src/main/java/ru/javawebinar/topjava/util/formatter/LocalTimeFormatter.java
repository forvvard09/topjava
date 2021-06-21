package ru.javawebinar.topjava.util.formatter;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String s, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalTime(s);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return String.valueOf(localTime);
    }
}
