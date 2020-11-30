package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Map<Integer, Meal>> repositoryUserMeal = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, Meal> repositoryMeals;

    {
        MealsUtil.meals.forEach(meal -> save(SecurityUtil.authUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        repositoryMeals = repositoryUserMeal.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repositoryMeals.put(meal.getId(), meal);
            return meal;
        }
        return repositoryMeals.computeIfPresent(meal.getId(), (id, oldUser) -> meal);
    }


    @Override
    public boolean delete(int userId, int id) {
        boolean result = false;
        repositoryMeals = repositoryUserMeal.getOrDefault(userId, null);
        if (repositoryMeals != null && repositoryMeals.remove(id) != null) {
            result = true;
            if (repositoryMeals.isEmpty()) {
                repositoryUserMeal.remove(userId);
            }
        }
        return result;
    }

    @Override
    public Meal get(int userId, int id) {
        repositoryMeals = repositoryUserMeal.getOrDefault(userId, null);
        return repositoryMeals != null ? repositoryMeals.getOrDefault(id, null) : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        repositoryMeals = repositoryUserMeal.getOrDefault(userId, null);
        return repositoryMeals == null ? Collections.emptyList() : repositoryMeals.values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }
}

