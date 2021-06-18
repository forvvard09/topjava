package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.setAuthUserId;

public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    MealService service;

    @Test
    void getAll() throws Exception {
        setAuthUserId(USER_ID);
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(getTos(meals, DEFAULT_CALORIES_PER_DAY)));
    }

    @Test
    void get() throws Exception {
        setAuthUserId(ADMIN_ID);
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_MEAL_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(adminMeal1));
    }

    @Autowired
    MealService mealService;

    @Test
    void delete() throws Exception {
        setAuthUserId(ADMIN_ID);
        perform(MockMvcRequestBuilders.delete(REST_URL + ADMIN_MEAL_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(ADMIN_MEAL_ID, ADMIN_ID));
    }

    @Test
    void createWithLocation() throws Exception {
        setAuthUserId(USER_ID);
        Meal newMeal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = readFromJson(action, Meal.class);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        setAuthUserId(USER_ID);
        Meal updateMeal = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updateMeal)))
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updateMeal);
    }

    @Test
    void getBetweenInclusive() throws Exception {

        final String START_DATE_TIME = "2020-01-30T00:00:01";
        final String END_DATE_TIME = "2020-01-30T23:59:01";
        final String urlFilter = String.format("filter?startDateTime=%s&endDateTime=%s", START_DATE_TIME, END_DATE_TIME);

        setAuthUserId(USER_ID);
        List<Meal> meals = service.getBetweenInclusive(LocalDateTime.parse(START_DATE_TIME).toLocalDate(), LocalDateTime.parse(END_DATE_TIME).toLocalDate(), USER_ID);
        perform(MockMvcRequestBuilders.get(REST_URL + urlFilter))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(getFilteredTos(meals, authUserCaloriesPerDay(), LocalDateTime.parse(START_DATE_TIME).toLocalTime(), LocalDateTime.parse(END_DATE_TIME).toLocalTime())));
    }
}
