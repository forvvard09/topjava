<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ru">
<head>
    <meta charset="UTF-8">
    <script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>
    <link rel="shortcut icon" href="img/favicon.ico">
    <title>List of meal</title>
    <style>
        .redd {
            color: red;
        }

        .greenn {
            color: green;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meal list</h2>
<table cellspacing="2" border="1" cellpadding="8">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="meal" items="${meals}">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
    <tr class= ${meal.isExcess() ? "redd" : "greenn"}>
        <td align="center">
            <fmt:parseDate value="${meal.getDateTime()}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
            <fmt:formatDate value="${parsedDate}" type="date" pattern="yyyy.MM.dd HH:mm"/>
        </td>
        <td align="center">${meal.getDescription()}</td>
        <td align="center">${meal.getCalories()}</td>

        <td><a href="meals?id=${meal.id}&action=edit"><img src="img/pencil.png"></a></td>
        <td><a href="meals?id=${meal.id}&action=delete"><img src="img/delete.png"></a></td>
    <tr>
        </c:forEach>
</table>

<!-- Or (no need requestScope) второй способ -->
<%--<jsp:useBean id="meals" scope="request" type="java.util.List"/>
<c:forEach items="${meals}" var="meal">
    ${meal.getDescription()}
</c:forEach>--%>
</br>
<span class="meal-addMeal" style="margin-left: 200px">
    <button class="addMeal"><img src="img/add.png" width="20" height="20"><br>Add meal</button>
</span>
</body>
</html>

<script>
    $(document).ready(function () {
        $(".addMeal").click(function () {
            console.log("Добавляем новую еду...");
            window.location.href = "meals?action=add";
        });
    });
</script>