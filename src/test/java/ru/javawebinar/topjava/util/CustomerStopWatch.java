package ru.javawebinar.topjava.util;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import ru.javawebinar.topjava.service.MealService;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class CustomerStopWatch extends Stopwatch {

    private static final Logger log = getLogger("result");

    private static StringBuilder totalMessage = new StringBuilder(String.format("%s,--> %s, %s ", System.lineSeparator(), "Total load test info: ", System.lineSeparator()));

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        String message = String.format("   >>> Test %s %s, load time %d microseconds",
                testName, status, TimeUnit.NANOSECONDS.toMicros(nanos));
        log.info(message);
        totalMessage.append(message).append(System.lineSeparator());
    }

    public static String getMessageAllTests() {
        return totalMessage.toString();
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, "finished", nanos);
    }
}
