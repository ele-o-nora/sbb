<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connections</title>
</head>
<body>
<ul>
<c:forEach var="train" items="${trains}">
    <li>
        Route: ${train.route} Direction: ${train.destination}
        <c:forEach var="stop" items="${trains.stops}">
            <c:if test="${stop.station.name = origin}">
                Departs: ${stop.departure}
            </c:if>
        </c:forEach>
        <c:forEach var="stop" items="${trains.stops}">
            <c:if test="${stop.station.name = destination}">
                Arrives: ${stop.arrival}
            </c:if>
        </c:forEach>
    </li>
</c:forEach>
</ul>
</body>
</html>
