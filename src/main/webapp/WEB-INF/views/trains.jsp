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
<%@include file="header.jsp" %>
<h2>Trains from ${origin} to ${destination}</h2>
<ul>
    <c:if test="${!empty trains}">
        <c:forEach var="train" items="${trains}">
            <li>
                Route: ${train.route} Direction: ${train.destination}
                <c:forEach var="stop" items="${train.stops}">
                    <c:if test="${stop.station eq origin}">
                        Departs from ${origin}: ${stop.departure}
                    </c:if>
                </c:forEach>
                <c:forEach var="stop" items="${train.stops}">
                    <c:if test="${stop.station eq destination}">
                        Arrives at ${destination}: ${stop.arrival}
                    </c:if>
                </c:forEach>
            </li>
        </c:forEach>
    </c:if>
    <c:if test="${!empty connections}">
        <c:forEach var = "connection" items="${connections}">
            <ul>
                <li>
                    Route: ${connection.firstTrain.route}
                    Direction ${connection.firstTrain.destination}
                    <c:forEach var="stop" items="${connection.firstTrain.stops}">
                        <c:if test="${stop.station eq origin}">
                            Departs from ${origin}: ${stop.departure}
                        </c:if>
                    </c:forEach>
                    <c:forEach var="stop" items="${connection.firstTrain.stops}">
                        <c:if test="${stop.station eq connection.transferStation}">
                            Arrives at ${connection.transferStation}: ${stop.arrival}
                        </c:if>
                    </c:forEach>
                </li>
                <li>
                    Route: ${connection.secondTrain.route}
                    Direction ${connection.secondTrain.destination}
                    <c:forEach var="stop" items="${connection.secondTrain.stops}">
                        <c:if test="${stop.station eq connection.transferStation}">
                            Departs from ${connection.transferStation}: ${stop.departure}
                        </c:if>
                    </c:forEach>
                    <c:forEach var="stop" items="${connection.secondTrain.stops}">
                        <c:if test="${stop.station eq destination}">
                            Arrives at ${destination}: ${stop.arrival}
                        </c:if>
                    </c:forEach>
                </li>
            </ul>
        </c:forEach>
    </c:if>
</ul>
</body>
</html>
