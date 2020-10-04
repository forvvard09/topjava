<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Meals</title>
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
<h2>Meals</h2>
<table cellspacing="2" border="1" cellpadding="5">
    <tr>
        <th>Дата</th>
        <th>Описание</th>
        <th>Количество каллорий</th>
    </tr>
    <c:forEach items="${requestScope.meals}" var="meal">
        <tr class= ${meal.isExcess() ? "greenn" : "redd"}>
            <td align="center">
                <fmt:parseDate value="${meal.getDateTime()}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
                <fmt:formatDate value="${parsedDate}" type="date" pattern="yyyy.MM.dd HH:mm"/>
            </td>
            <td align="center">${meal.getDescription()}</td>
            <td align="center">${meal.getCalories()}</td>

            <td><a href="&action=edit"><img src="img/pencil.png"></a></td>
            <td><a href=&action=delete"><img src="img/delete.png"></a></td>
        </tr>
    </c:forEach>

    <!-- Or (no need requestScope) второй способ -->
    <%--<jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach items="${meals}" var="meal">
        ${meal.getDescription()}
    </c:forEach>--%>

</table>
<div class="create-new-meal" style="margin-left: 30px">
    <button class="addNewMeal"><img src="img/add.png">Добавить прием пищи</button>
</div>
</br>


</body>
</html>