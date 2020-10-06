package ru.javawebinar.topjava.storage;


import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MealsMemoryStorage extends AbstractStorage<String> {


    private final Map<String, Meal> mapMeal = new ConcurrentHashMap<>();

    @Override
    protected void doSave(String key, Meal newMeal) {
        mapMeal.put(key, newMeal);
    }

    @Override
    protected Meal getFromStorage(String searchKey) {
        return mapMeal.get(searchKey);
    }

    @Override
    protected void doUpdate(String searchKey, Meal newMeal) {
        Meal updateMeal = getFromStorage(searchKey);
        updateMeal.setDateTime(newMeal.getDateTime());
        updateMeal.setDescription(newMeal.getDescription());
        updateMeal.setCalories(newMeal.getCalories());
    }

    @Override
    protected void doDelete(String searchKey) {
        mapMeal.remove(searchKey);
    }

    @Override
    protected List<Meal> doCopyAll() {
        return new ArrayList<>(mapMeal.values());
    }
}
