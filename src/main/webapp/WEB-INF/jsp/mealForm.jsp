<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="fragments/globalValue.jsp"/>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<head>
    <title><spring:message code="meal.caption"/></title>
</head>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <spring:message code="meal.edit" var="operationEdit"/>
    <spring:message code="meal.create" var="operationCreate"/>

    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <h3>${meal.isNew() ? operationCreate : operationEdit}</h3>
    <form method="post" action="${mealsURL}/create-update">

        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal.create.date"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.create.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.create.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="meal.create.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="meal.create.cancel"/></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
