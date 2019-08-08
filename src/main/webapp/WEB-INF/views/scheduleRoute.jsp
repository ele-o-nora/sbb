<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Route scheduling pattern</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h4 class="text-secondary m-4">Scheduling pattern for route <span class="text-danger">${routeNumber}</span></h4>
<span class="text-muted m-3">Enter stop duration in minutes</span>
<form method="post"
      <c:if test="${empty route}">action="${pageContext.request.contextPath}/admin/addNewRoute"</c:if>
      <c:if test="${!empty route}">action="${pageContext.request.contextPath}/admin/modifyRoute"</c:if>>
    <c:if test="${!empty route}">
        <input type="hidden" value="${route.id}" name="routeId">
    </c:if>
    <c:if test="${empty route}">
        <input type="hidden" value="${routeNumber}" name="routeNumber">
        <input type="hidden" name="lineId" value="${line.id}">
    </c:if>
    <c:forEach var="station" items="${routeStations}" varStatus="status">
        <input type="hidden" name="stations" value="${station}">
        <div class="row m-4">
            <div class="col-sm-4 offset-2 pt-2">
            <span class="font-weight-bold align-bottom float-right">${station}</span>
            </div>
            <c:if test="${!status.first and !status.last}">
                <div class="col-sm-1">
                    <input type="text" class="form-control" name="waitTimes" pattern=""
                           value="15" required>
                </div>
            </c:if> <br/>
        </div>
    </c:forEach>
    <input type="submit" class="btn btn-outline-secondary" value="Create route scheduling pattern">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
</body>
</html>
