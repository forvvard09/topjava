
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <link rel="shortcut icon" href="img/favicon.ico">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Add meal</title>
    <title>Update meal</title>

</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>New meal</h2>

<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="uuid" value="${meal.uuid}">
    <dl>
        <dt>Дата и время:</dt>
        <dd><input type="datetime-local" name="dateTime" value="${meal.dateTime}"></dd>
    </dl>
    <dl>
        <dt>Описание:</dt>
        <dd><input type="text" name="description" size="77" value="${meal.description}"></dd>
    </dl>
    <dl>
        <dt>Количество каллорий:</dt>
        <dd><input type="number" name="countCallories" size="10" value="${meal.calories}"></dd>
    </dl>
    <hr>
    </br>
    <hr>
    <div class="buttons">
        <button class="saveMeal" style="margin-left: 30px" type="submit">Сохранить</button>
        <button class="cancelMeal" style="margin-left: 90px" onclick="window.history.back()">Отмена</button>
        <button class="cancelMealTest" style="margin-left: 90px" onclick="history.back(); return false;">Отмена</button>

    </div>
</form>

</body>
</html>

