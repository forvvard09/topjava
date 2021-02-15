package ru.javawebinar.topjava.util;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class CustomerStopWatch extends Stopwatch {

    private static final Logger log = getLogger(CustomerStopWatch.class);

    private static final List<String> testsLoadMessage = new ArrayList<>();

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        String message = String.format("Test %s %s, spent %d microseconds",
                testName, status, TimeUnit.NANOSECONDS.toMicros(nanos));
        testsLoadMessage.add(message);
        log.debug(message);
    }

    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, "succeeded", nanos);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, "failed", nanos);
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        logInfo(description, "skipped", nanos);
    }

    public static List<String> getAllTestsResult() {
        return new ArrayList<>(testsLoadMessage);
    }

    /*
    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, "finished", nanos);
    }
     */
}
