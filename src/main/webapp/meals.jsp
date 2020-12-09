<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <meta charset="UTF-8">
    <script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>

    <hr>
    <section>
        <form id="meal_filtres_day" method="get" action="meals">
            <input type="hidden" value="filter-day" name="action">
            <p>
            <h1>Filter day:</h1></p>

            <label>Start day (>=)</label>
            <dd><input type="date" value="" name="startDay"></dd>

            <label>End day (>=)</label>
            <dd><input type="date" value="" name="endDay"></dd>
            <br>
            <button type="submit" style="margin-left: 100px">Фильтрация по дянм</button>
        </form>
    </section>
    <hr>
    <section>
        <form id="mealTo_filtres_time" method="get" action="meals">
            <input type="hidden" value="filter-time" name="action">
            <p>
            <h1>Filter time:</h1></p>

            <label>Start time(>=)</label>
            <dd><input type="time" value="" name="startTime"></dd>

            <label>End time (<)</label>
            <dd><input type="time" value="" name="endTime"></dd>
            <br>
            <button type="submit" style="margin-left: 100px">Фильтрация по времени</button>
        </form>
    </section>
    <hr>
    <br>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>