<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Station schedule</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h4 class="m-4">Current schedule for ${stationName}</h4>
<div class="row">
    <div class="col-sm-8 offset-sm-2">
        <table class="table">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Route</th>
                <th scope="col">Direction</th>
                <th scope="col">Arrives</th>
                <th scope="col">Departs</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="train" items="${trains}">
                <tr>
                    <td>${train.route}</td>
                    <td>${train.direction}</td>
                    <td>${train.arrival}</td>
                    <td>${train.departure}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
