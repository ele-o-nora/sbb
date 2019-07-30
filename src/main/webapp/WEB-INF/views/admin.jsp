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
<body class="text-center">
<%@include file="header.jsp" %>
<h4 class="text-secondary m-4">Westerosi Railways admin panel</h4>
<div class="row">
    <div class="col-sm-6 offset-3">
        <div id="addStationMenu" class="bg-dark text-light m-3 rounded">Add new station</div>
    </div>
</div>
<div class="row">
    <div class="col-sm-4 offset-4">
        <ul id="linesList" class="list-group">
            <c:forEach var="line" items="${lines}">
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/admin/addStation/${line.id}"
                       class="text-danger stretched-link">${line.name} line</a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<div class="row">
    <div class="col-sm-6 offset-3">
        <div id="addTrainMenu" class="bg-dark text-light m-3 rounded">Add new train model</div>
    </div>
</div>
<form action="${pageContext.request.contextPath}/admin/addTrain" method="post" id="addTrainForm">
    <div class="form-row">
        <div class="col-sm-2 offset-4">
            <input type="text" name="model" placeholder="Model name" class="form-control" required>
        </div>
        <div class="col-sm-1">
            <input type="text" name="seats" placeholder="Seats" class="form-control" required>
        </div>
        <div class="col-sm-1">
            <input type="submit" value="Add" class="btn btn-outline-secondary">
        </div>
    </div>
</form>

</body>
</html>
