package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String requestAnswer = request.getParameter("userId");
        if (requestAnswer == null) {
            log.debug("User controller: forward to meals users userId");
            request.getRequestDispatcher("/users.jsp").forward(request, response);
        } else {
            int userId = Integer.parseInt(requestAnswer);
            SecurityUtil.setAuthUserId(userId);
            log.debug("User controller: redirect to meals with userId");
            response.sendRedirect("meals");
        }
    }
}
