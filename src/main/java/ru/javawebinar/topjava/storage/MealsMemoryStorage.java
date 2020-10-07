package ru.javawebinar.topjava.storage;


import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.MealServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;


public class MealsMemoryStorage implements Storage<Integer> {

    private final Map<Integer, Meal> mapMeal;
    private final AtomicInteger counter;

    private static final Logger log = getLogger(MealServlet.class);

    public MealsMemoryStorage() {
        this.counter = new AtomicInteger(0);
        this.mapMeal = new ConcurrentHashMap<>();
    }

    @Override
    public void save(Meal meal) {
        log.debug("Save to storage meal: " + meal);
        meal.setId(getCount());
        log.debug("Save to storage meal, new id: " + meal.getId());
        mapMeal.put(meal.getId(), meal);
    }

    @Override
    public Meal get(Integer key) {
        log.debug("Get meal from storage, key: " + key);
        return mapMeal.get(key);
    }

    @Override
    public void delete(Integer key) {
        log.debug("Remove meal by key: " + key);
        mapMeal.remove(key);
    }

    @Override
    public void update(Meal newMeal) {
        log.debug("Update meal: " + newMeal);
        mapMeal.remove(newMeal.getId());
        mapMeal.put(newMeal.getId(), newMeal);
    }

    @Override
    public List<Meal> getAll() {
        log.debug("Get all from storage");
        return new ArrayList<>(mapMeal.values());
    }

    private int getCount() {
        log.debug("Storage, generate new id");
        return this.counter.incrementAndGet();
    }
}
