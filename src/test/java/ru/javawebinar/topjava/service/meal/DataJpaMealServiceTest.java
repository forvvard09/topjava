package ru.javawebinar.topjava.service.meal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Autowired
    protected MealService mealService;

    @Autowired
    protected UserService userService;


    @Test
    public void getWithUser() {
        Meal actualMeal = mealService.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actualMeal, adminMeal1);
        USER_MATCHER.assertMatch(actualMeal.getUser(), userService.get(ADMIN_ID));
    }

    @Test
    public void getWithUserNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.getWithUser(NOT_FOUND, NOT_FOUND));
    }

    @Test
    public void getWithUserNotOwner() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.getWithUser(ADMIN_MEAL_ID, NOT_FOUND));
    }
}