<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin panel</title>
</head>
<body>
<%@include file="header.jsp" %>
<div id="addStationMenu">Add new station</div>
    <ul id="linesList">
        <c:forEach var="line" items="${lines}">
            <li><a href="${pageContext.request.contextPath}/admin/addStation/${line.id}">${line.name}</a></li>
        </c:forEach>
    </ul>
<div id="addTrainMenu">Add new train model</div>
<form action="${pageContext.request.contextPath}/admin/addTrain" method="post" id="addTrainForm">
    <input type="text" name="model" placeholder="Model name" required>
    <input type="text" name="seats" placeholder="Seats quantity" required>
    <input type="submit" value="Add">
</form>
</body>
</html>
