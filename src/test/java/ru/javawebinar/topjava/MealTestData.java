package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData extends TestsData<Meal> {

    public static final int MEAL_ID = START_SEQ + 2;

    public static final Meal MEAL_ONE = new Meal(MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_TWO = new Meal(MEAL_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_THREE = new Meal(MEAL_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL_FOUR = new Meal(MEAL_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal MEAL_FIVE = new Meal(MEAL_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL_SIX = new Meal(MEAL_ID + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal MEAL_SEVEN = new Meal(MEAL_ID + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    public static final List<Meal> allMeal = Arrays.asList(MEAL_SEVEN, MEAL_SIX, MEAL_FIVE, MEAL_FOUR, MEAL_THREE, MEAL_TWO, MEAL_ONE);
    public static final List<Meal> restrictedListMeal = Arrays.asList(MEAL_THREE, MEAL_TWO, MEAL_ONE);

    public static Meal getNewMeal() {
        return new Meal(LocalDateTime.of(2021, Month.JANUARY, 1, 10, 0), "Пир", 410);
    }

    public static Meal getUpdatedMeal() {
        Meal mealUpdated = new Meal(MEAL_ONE);
        mealUpdated.setDescription("UpdatedDescription");
        mealUpdated.setCalories(999);
        return mealUpdated;
    }
}
