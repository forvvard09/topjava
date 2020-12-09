package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void delete(int userId, int id) {
        checkNotFoundWithId(repository.delete(userId, id), id, "MealService");
    }

    public Meal get(int userId, int id) {
        return checkNotFoundWithId(repository.get(userId, id), id, "MealService");
    }

    //model -> dto
    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> getFilterTime(int userId, int caloriesPerDay, LocalDateTime startTime, LocalDateTime endTime) {
        return MealsUtil.getFilteredTos(repository.getAll(userId), caloriesPerDay, startTime, endTime);
    }


    public void update(int userId, Meal meal) {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId(), "MealService");
    }

    public List<MealTo> getFilterDay(int userId, LocalDateTime startDay, LocalDateTime endDay, int caloriesPerDay) {
        Collection<Meal> listMealBetween = repository.getBetweenHalfOpen(userId, startDay, endDay);
        return MealsUtil.getTos(listMealBetween, caloriesPerDay);
    }
}