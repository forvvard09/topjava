<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="globalValue.jsp"/>

<header>
    <a href="${mealsURL}"><spring:message code="app.title"/></a> | <a href="${baseURL}/users"><spring:message
        code="user.title"/></a> | <a href="${baseURL}"><spring:message code="app.home"/></a>
</header>