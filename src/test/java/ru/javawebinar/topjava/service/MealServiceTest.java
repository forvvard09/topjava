package ru.javawebinar.topjava.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.TestsMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private static TestsMatcher<Meal> testsMatcher;

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @BeforeClass
    public static void setup() {
        testsMatcher = new TestsMatcher<Meal>();
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getNewMeal();
        Meal createdMeal = service.create(newMeal, USER_ID);
        int newMealId = createdMeal.getId();
        newMeal.setId(newMealId);
        testsMatcher.assertMatch(createdMeal, newMeal);
        testsMatcher.assertMatch(service.get(newMealId, USER_ID), newMeal);
    }

    @Test
    public void createErrorDuplicateDateCreate() throws Exception {
        LocalDateTime existingDateCreate = service.get(MEAL_ID, USER_ID).getDateTime();
        Meal newMeal = getNewMeal();
        newMeal.setDateTime(existingDateCreate);
        assertThrows(DuplicateKeyException.class, () -> service.create(newMeal, USER_ID));
    }

    @Test
    public void get() throws Exception {
        Meal meal = service.get(MEAL_ID, USER_ID);
        testsMatcher.assertMatch(meal, MEAL_ONE);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getErrorMealOtherUser() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, NOT_FOUND));
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deleteErrorMealOtherUser() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, NOT_FOUND));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusiveEmptyBorders() throws Exception {
        List<Meal> listMeal = service.getBetweenInclusive(null, null, USER_ID);
        testsMatcher.assertMatch(listMeal, allMeal);
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        List<Meal> listMeal = service.getBetweenInclusive(MEAL_THREE.getDate(), MEAL_THREE.getDate(), USER_ID);
        testsMatcher.assertMatch(listMeal, restrictedListMeal);
    }

    @Test
    public void getBetweenInclusiveMealOtherUser() throws Exception {
        List<Meal> listMeal = service.getBetweenInclusive(null, null, NOT_FOUND);
        testsMatcher.assertEmpty(listMeal);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> listMeal = service.getAll(USER_ID);
        testsMatcher.assertMatch(listMeal, allMeal);
    }

    @Test
    public void update() throws Exception {
        Meal updatedMeal = getUpdatedMeal();
        service.update(updatedMeal, USER_ID);
        testsMatcher.assertMatch(service.get(updatedMeal.getId(), USER_ID), updatedMeal);
    }

    @Test
    public void getErrorUpdateMealOtherUser() throws Exception {
        Meal updatedMeal = getUpdatedMeal();
        assertThrows(NotFoundException.class, () -> service.update(updatedMeal, NOT_FOUND));
    }

    @Test
    public void getUpdateNotFound() throws Exception {
        Meal updatedMeal = getUpdatedMeal();
        updatedMeal.setId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updatedMeal, USER_ID));
    }
}