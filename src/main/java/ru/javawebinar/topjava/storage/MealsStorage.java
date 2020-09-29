package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MealsStorage {

    final private List<Meal> listMeal;

    public MealsStorage(List<Meal> listMeal) {
        this.listMeal = listMeal;
    }

    public List<Meal> getListMeal() {
        return listMeal;
    }

    public void fillListMeals() {

    }
}
