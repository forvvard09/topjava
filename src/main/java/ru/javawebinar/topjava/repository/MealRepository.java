package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MealRepository {

    // null if not found, when updated
    Meal save(int userId, Meal meal);

    // false if not found
    boolean delete(int userId, int id);

    // null if not found
    Meal get(int userId, int id);

    Collection<Meal> getAll(int userId);

    Collection<Meal> getBetweenHalfOpen(int userId, LocalDateTime startDay, LocalDateTime endDay, LocalDateTime startTime, LocalDateTime endTime);

}
