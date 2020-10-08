package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {

    void save(final Meal meal);

    Meal get(final int key);

    void delete(final int key);

    void update(final Meal newMeal);

    List<Meal> getAll();
}
