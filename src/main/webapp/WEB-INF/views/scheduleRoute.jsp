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
        ${station}
        <c:if test="${!status.first and !status.last}">
            <input type="text" class="form-control" name="waitTimes" placeholder="Time at station">
        </c:if> <br/>
        <c:if test="${!status.last}">
            <input type="text" class="form-control" name="timesEnRoute" placeholder="Time en route"><br/>
        </c:if>
    </c:forEach>
    <input type="submit" class="btn btn-outline-secondary" value="Create route scheduling pattern">
</form>
</body>
</html>
