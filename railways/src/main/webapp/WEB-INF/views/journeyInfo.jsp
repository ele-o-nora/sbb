<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detailed journey info</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h5 class="text-secondary m-3"><span class="text-danger">${journey.route}</span> direction
    <span class="text-danger">${journey.destination}</span></h5>
<h6 class="m-3">Train model: ${journey.trainType.model}</h6>
<div class="row justify-content-center">
    <div class="col-sm-8">
        <table class="table">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Station</th>
                <th scope="col">Arrives</th>
                <th scope="col">Departs</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="stop" items="${journey.stops}">
                <tr>
                    <th scope="row" class="align-middle">${stop.station}</th>
                    <td class="align-middle">
                        <c:if test="${!empty stop.arrival}">
                            ${stop.arrival}
                        </c:if>
                        <c:if test="${empty stop.arrival}">-</c:if>
                    </td>
                    <td class="align-middle">
                        <c:if test="${!empty stop.departure}">
                            ${stop.departure}
                        </c:if>
                        <c:if test="${empty stop.departure}">-</c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<ul>

</ul>
</body>
</html>
