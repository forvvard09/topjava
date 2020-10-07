package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage<SK> {

    void save(final Meal meal);

    Meal get(final SK key);

    void delete(final SK key);

    void update(final Meal newMeal);

    List<Meal> getAll();
}
