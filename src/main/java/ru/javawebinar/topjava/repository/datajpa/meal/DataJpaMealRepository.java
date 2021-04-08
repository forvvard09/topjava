package ru.javawebinar.topjava.repository.datajpa.meal;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.datajpa.usesr.CrudUserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRMealepository;

    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRMealepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(this.crudUserRepository.getOne(userId));
        if (meal.isNew()) {
            return this.crudRMealepository.save(meal);
        } else if (get(meal.id(), userId) == null) {
            return null;
        }
        return this.crudRMealepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return this.crudRMealepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = this.crudRMealepository.findById(id).orElse(null);
        return meal != null && meal.getUser().getId() != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        var meals = this.crudRMealepository.findAll();

        return meals.isEmpty() ? Collections.emptyList() :
                meals.stream()
                        .filter(meal -> meal.getUser().getId() != null && meal.getUser().getId() == userId)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return this.crudRMealepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
