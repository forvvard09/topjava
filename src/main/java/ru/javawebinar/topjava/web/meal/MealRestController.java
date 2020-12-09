package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    /* можно использовать конуструктор, тогда не нужно указывать @Autowired

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }
     */

    public List<MealTo> getAll() {
        int userId = SecurityUtil.getAuthUserId();
        log.info("getAll for meal for user, userId {}", userId);
        return service.getAll(userId, SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getFilterTime(LocalDateTime startTime, LocalDateTime endTime) {
        int userId = SecurityUtil.getAuthUserId();
        log.info("getAll for meal for user, userId {}", userId);
        return service.getFilterTime(userId, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public List<MealTo> getFilterDay(LocalDateTime startDay, LocalDateTime endDay, LocalDateTime startTime, LocalDateTime endTime) {
        int userId = SecurityUtil.getAuthUserId();
        log.info("getFilterDay startDay {}, endDay {}, startTime {}, endTime {} ", startDay, endDay, startTime, endTime);
        return service.getFilterDay(userId, startDay, endDay, SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        int userId = SecurityUtil.getAuthUserId();
        log.info("get meal by id for user, userId, id {} {}", userId, id);
        return service.get(userId, id);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.getAuthUserId();
        log.info("create meal for user, userId, meal {} {}", userId, meal);
        checkNew(meal);
        return service.create(userId, meal);
    }

    public void delete(int id) {
        int userId = SecurityUtil.getAuthUserId();
        log.info("delete meal, userId, id {} {}", userId, id);
        service.delete(userId, id);
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.getAuthUserId();
        log.info("update meal {} with id {}", meal, meal.getId());
        assureIdConsistent(meal, id);
        service.update(userId, meal);
    }
}