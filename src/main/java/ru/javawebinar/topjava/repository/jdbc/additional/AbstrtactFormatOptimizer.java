package ru.javawebinar.topjava.repository.jdbc.additional;

import java.time.LocalDateTime;

public abstract class AbstrtactFormatOptimizer<T> {
     public abstract T doDateForDB(LocalDateTime inputDateTime);
}
