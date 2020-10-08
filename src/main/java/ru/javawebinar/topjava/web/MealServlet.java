package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealsMemoryStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.MEALS;


public class MealServlet extends HttpServlet {

    private Storage storage;

    private static final LocalTime MAX_TIME = LocalTime.MAX;
    private static final LocalTime MIN_TIME = LocalTime.MIN;

    private static final Logger log = getLogger(MealServlet.class);

    @Override
    public void init() throws ServletException {
        log.debug("Init servlet");
        super.init();
        storage = new MealsMemoryStorage();
        MEALS.forEach(storage::save);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // принимаем русские буквы
        String id = request.getParameter("id");
        LocalDateTime dateTime = TimeUtil.convertToDateTime(request.getParameter("dateTime"));
        int callories = transformationId(request.getParameter("callories"));
        String description = request.getParameter("description");
        if (id.equals("-1")) {
            log.debug("Do post. Save new meal.");
            storage.save(new Meal(dateTime, description, callories));
        } else {
            log.debug("Do post. Update new meal.");
            storage.update(new Meal(transformationId(id), dateTime, description, callories));
        }
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if (action == null) {
            log.debug("Do get. Redirect to meals");
            request.setAttribute("meals", MealsUtil.filteredByStreams(storage, MIN_TIME, MAX_TIME, 2000));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        Meal meal;
        switch (action) {
            case ("delete"):
                log.debug("Do get. Choice delete");
                storage.delete(transformationId(id));
                response.sendRedirect("meals");
                return;
            case ("add"):
                log.debug("Do get. Choice add meal");
                meal = new Meal();
                break;
            case ("edit"):
                log.debug("Do get. Choice edit meal");
                meal = storage.get(transformationId(id));
                break;
            default:
                throw new IllegalArgumentException("Action " + action + "is legal.");
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/create_edit.jsp").forward(request, response);
    }

    private int transformationId(String inputId) {
        return Integer.parseInt(inputId);
    }
}