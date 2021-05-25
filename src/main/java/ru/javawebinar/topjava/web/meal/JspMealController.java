package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {


    @GetMapping("")
    public String getAllMeals(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/delete")
    public String deleteMeal(@RequestParam("id") int id) {
        //можем использовать другой способ получения id, как в сервлетах, считается устаревшим
      /*
      public String delete(HttpServletRequest request) {
      int id = Integer.parseInt(request.getParameter("id"));
       */
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam("id") int id) {
        model.addAttribute("meal", super.get(id));
        return "mealForm";
    }

    @PostMapping("/create-update")
    public String createUpdate(@RequestParam(value = "id", defaultValue = "") String id,
                               @RequestParam("dateTime") String dateTime,
                               @RequestParam("description") String description,
                               @RequestParam("calories") int calories
    ) {
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);
        if (id.isEmpty()) {
            super.create(meal);
        } else {
            meal.setId(Integer.valueOf(id));
            super.update(meal, meal.id());
        }
        return "redirect:/meals";
    }

    @PostMapping("/filter")
    public String filter(Model model, @RequestParam("startDate") String startDate,
                         @RequestParam("endDate") String endDate,
                         @RequestParam("startTime") String startTime,
                         @RequestParam("endTime") String endTime
    ) {
        List<MealTo> mealsTo = super.getBetween(parseLocalDate(startDate), parseLocalTime(startTime), parseLocalDate(endDate), parseLocalTime(endTime));
        model.addAttribute("meals", mealsTo);
        return "meals";
    }
}
