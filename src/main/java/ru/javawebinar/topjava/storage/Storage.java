package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {

    void save(final Meal meal);

    Meal get(final String key);

    void delete(final String key);

    void update(final Meal newMeal);

    List<Meal> getAll();
}
