<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <link rel="shortcut icon" href="img/favicon.ico">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>
    <title>${meal.id == 0 ? "Add" : "Update"}&nbsp;meal</title>
    <style>
        dl {
            background: none repeat scroll 0 0 #FAFAFA;
            margin: 8px 0;
            padding: 0;
        }

        dt {
            display: inline-block;
            width: 170px;
        }

        dd {
            display: inline-block;
            margin-left: 8px;
            vertical-align: top;
        }
    </style>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<hr>

<h2>${meal.id == 0 ? "Create" : "Edit"}&nbsp;meal</h2>

<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="id" value="${meal.id}">
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
        <dd><input type="number" name="callories" size="5" value="${meal.calories}"></dd>
    </dl>
    </br>
    <hr align="left" width="20%">
    <div class="buttons">
        <button class="saveMeal" style="margin-left: 30px; visibility: ${meal.id == 0 ? "hidden" : "visible"}"
                type="submit">Save
        </button>
        <button class="cancelMeal" style="margin-left: 90px" type="button" onclick="window.history.back()">Cancel
        </button>
    </div>
</form>
</body>
</html>

<script>
    $(document).ready(function () {
        $("input[name='dateTime']").on('input', function () {
            if ($("input[name='dateTime']").val() !== "" & $("input[name='callories']").val() !== "") {
                $(".saveMeal").css('visibility', 'visible');
            }
        });
    });
</script>