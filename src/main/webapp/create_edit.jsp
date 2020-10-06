<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <link rel="shortcut icon" href="img/favicon.ico">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>
    <title>Add and Update meal</title>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<span class="editMeal">
    <h2>Edit meal</h2>
</span>
<span class="createMeal">
    <h2>CreateMeal</h2>
</span>

<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="uuid" value="${meal.uuid}">
    <dl>
        <dt>Дата и время:</dt>
        <dd><input type="datetime-local" name="dateTime" value="${meal.dateTime}"></dd>
    </dl>
    <dl>
        <dt>Описание:</dt>
        <dd><input type="text" name="description" size="50" value="${meal.description}"></dd>
    </dl>
    <dl>
        <dt>Количество каллорий:</dt>
        <dd><input type="number" name="countCallories" size="5" value="${meal.calories}"></dd>
    </dl>
    </br>
    <hr align="left" width="20%">
    <div class="buttons">
        <button class="saveMeal" style="margin-left: 30px; visibility: hidden" type="submit">Сохранить</button>
        <button class="cancelMeal" style="margin-left: 90px" type="button" onclick="window.history.back()">Отмена
        </button>
    </div>
</form>
</body>
</html>

<script>
    $(document).ready(function () {
        if ($("input[name='uuid']").val() !== "") {
            $(".createMeal").remove();
        } else {
            $(".editMeal").remove();
        }

        if ($("input[name='dateTime']").val() !== "" & $("input[name='countCallories']").val() !== "") {
            $(".saveMeal").css('visibility', 'visible');
            ;
        }

        if ($("input[name='dateTime']").val() !== "" & $("input[name='countCallories']").val() !== "") {
            $(".saveMeal").css('visibility', 'visible');
            ;
        }

        $("input[name='dateTime']").on('input', function () {
            if ($("input[name='dateTime']").val() !== "" & $("input[name='countCallories']").val() !== "") {
                $(".saveMeal").css('visibility', 'visible');
                ;
            }
        });
    });
</script>
