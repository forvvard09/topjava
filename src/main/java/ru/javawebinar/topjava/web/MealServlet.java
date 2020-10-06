package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.config.Config;
import ru.javawebinar.topjava.model.Meal;
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


public class MealServlet extends HttpServlet {

    private Storage storage = Config.get().getStorage();

    private static final Logger log = getLogger(MealServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // принимаем русские буквы
        String uuid = request.getParameter("uuid");
        LocalDateTime dateTime = TimeUtil.convertToDateTime(request.getParameter("dateTime"));
        int countCallories = Integer.parseInt(request.getParameter("countCallories"));
        String description = request.getParameter("description");
        if (uuid.equals("")) {
            storage.save(new Meal(dateTime, description, countCallories));
        } else {
            storage.update(new Meal(uuid, dateTime, description, countCallories));
        }
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            log.debug("Do get. Redirect to meals");
            request.setAttribute("meals", MealsUtil.filteredByStreams(storage, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        Meal meal;
        switch (action) {
            case ("delete"):
                log.debug("Do get. Change delete");
                storage.delete(uuid);
                response.sendRedirect("meals");
                return;
            case ("add"):
                log.debug("Do get. Change add meal");
                meal = new Meal();
                break;
            case ("edit"):
                log.debug("Do get. Change add edit");
                meal = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + "is legal.");
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/create_edit.jsp").forward(request, response);
    }
}