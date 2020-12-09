package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_USERID;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Map<Integer, Meal>> repositoryUserMeal = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        SecurityUtil.setAuthUserId(DEFAULT_USERID);
        MealsUtil.meals.forEach(meal -> save(SecurityUtil.getAuthUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer, Meal> repositoryMeals = repositoryUserMeal.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repositoryMeals.put(meal.getId(), meal);
            return meal;
        }
        return repositoryMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> repositoryMeals = repositoryUserMeal.get(userId);
        return repositoryMeals != null && repositoryMeals.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        Map<Integer, Meal> repositoryMeals = repositoryUserMeal.get(userId);
        return repositoryMeals != null ? repositoryMeals.get(id) : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true);
    }

    @Override
    public Collection<Meal> getBetweenHalfOpen(int userId, LocalDateTime startDay, LocalDateTime endDay) {
        return filterByPredicate(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDay, endDay, null, null));
    }

    private Collection<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> repositoryMeals = repositoryUserMeal.get(userId);
        return repositoryMeals == null ? Collections.emptyList() : repositoryMeals.values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }
}