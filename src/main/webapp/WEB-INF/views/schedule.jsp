<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Schedule</title>
</head>
<body>
<h2>Schedule for ${stationName}</h2>
<ul>
    <c:forEach var="train" items="${trains}">
        <li>
            Route: ${train.route};
            Direction: ${train.direction};
            Arrives: ${train.arrival};
            Departs: ${train.departure}
        </li>
    </c:forEach>
</ul>
</body>
</html>
