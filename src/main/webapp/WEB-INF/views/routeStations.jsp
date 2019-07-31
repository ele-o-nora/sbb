<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Choose route stations</title>
</head>
<%@include file="header.jsp" %>
<body class="text-center">
<c:if test="${empty route}">
    <h4 class="text-secondary m-4">Add new route to ${line.name} line</h4>
</c:if>
<c:if test="${!empty route}">
    <h4 class="text-secondary m-4">Modify route <span class="text-danger">${route.number}</span> on ${line.name} line</h4>
</c:if>
<form action="${pageContext.request.contextPath}/admin/scheduleRoute" method="post">
    <input type="hidden" name="lineId" value="${line.id}">
    <c:if test="${!empty route}">
        <input type="hidden" name="routeNumber" value="${route.number}">
        <input type="hidden" name="routeId" value="${route.id}">
    </c:if>
    <div class="row text-left">
        <div class="col-sm-3 offset-5">
    <c:forEach var="station" items="${stations}">
        <c:set var="contains" value="false"/>
        <c:if test="${!empty route}">
            <c:forEach var="item" items="${route.stations}">
                <c:if test="${item eq station.name}">
                    <c:set var="contains" value="true"/>
                </c:if>
            </c:forEach>
        </c:if>
        <input type="checkbox" class="form-check-input" name="stations"
        value="${station.name}" <c:if test="${contains}">checked</c:if> >${station.name}<br/>
    </c:forEach>
    </div>
    </div>
    <div class="form-row m-3 justify-content-center">
        <c:if test="${empty route}">
            <div class="col-sm-4">
            <input type="text" class="form-control" placeholder="Route number" name="routeNumber" required>
            </div>
        </c:if>
        <input type="submit" class="btn btn-outline-secondary" value="Proceed to scheduling pattern">
    </div>
</form>
</body>
</html>
