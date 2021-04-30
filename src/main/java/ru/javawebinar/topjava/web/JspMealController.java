package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController {

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    private final MealService service;

    public JspMealController(MealService service) {
        this.service = service;
    }

    @GetMapping("")
    public String getAllMeals(Model model) {
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        log.info("getAll for user {}", SecurityUtil.authUserId());
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        int userId = SecurityUtil.authUserId();
        //можем использовать другой способ получения id, как в сервлетах, считается устаревшим
      /*
      public String delete(HttpServletRequest request) {
      int id = Integer.parseInt(request.getParameter("id"));

       */
        service.delete(id, userId);
        log.info("delete meal {} for user {}", id, userId);
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        log.info("create new meal");
        model.addAttribute("mealForm", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam("id") int id) {
        int userId = SecurityUtil.authUserId();
        log.info("update meal with id {} usesrId", id, userId);
        model.addAttribute("mealForm", service.get(id, userId));
        return "mealForm";
    }

    @PostMapping("/create-update")
    public String createUpdate(@RequestParam(value = "id", defaultValue = "") String id,
                               @RequestParam("dateTime") String dateTime,
                               @RequestParam("description") String description,
                               @RequestParam("calories") int calories
    ) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);
        if (id.isEmpty()) {
            service.create(meal, userId);
        } else {
            meal.setId(Integer.valueOf(id));
            service.update(meal, userId);
        }
        log.info("creat or edit meal for user {}", userId);
        return "redirect:/meals";
    }

    @PostMapping("/filter")
    public String filter(Model model, @RequestParam("startDate") String startDate,
                         @RequestParam("endDate") String endDate,
                         @RequestParam("startTime") String startTime,
                         @RequestParam("endTime") String endTime
    ) {
        int userId = SecurityUtil.authUserId();
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(parseLocalDate(startDate), parseLocalDate(endDate), userId);
        log.info("filter dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        model.addAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), parseLocalTime(startTime), parseLocalTime(endTime)));
        return "meals";
    }
}
