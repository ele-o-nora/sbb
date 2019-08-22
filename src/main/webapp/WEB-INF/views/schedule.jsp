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
<c:if test="${!empty error}">
    <div class="m-5">
        <span class="text-danger">${error}</span><br/>
        <a href="${pageContext.request.contextPath}/" class="text-secondary">Search anew</a>
    </div>
</c:if>
<c:if test="${empty error}">
    <h4 class="m-4">Station schedule for
        <span class="text-danger">${stationName}</span> from
        <span class="text-danger">${momentFrom}</span></h4>
    <c:if test="${empty trains}">
        <div class="m-5">
            <span>There are no trains scheduled to be at the station at this time.</span><br/>
            <a href="${pageContext.request.contextPath}/" class="text-danger">Back to main page</a>
        </div>
    </c:if>
    <c:if test="${!empty trains}">
    <div class="row">
        <div class="col-sm-8 offset-sm-2">
            <table class="table">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">Route</th>
                    <th scope="col">Direction</th>
                    <th scope="col">Arrives</th>
                    <th scope="col">Departs</th>
                    <th scope="col">Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="train" items="${trains}">
                    <tr>
                        <td>${train.route}</td>
                        <td>${train.direction}</td>
                        <td>${train.arrival}</td>
                        <td>${train.departure}</td>
                        <td>${train.status}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    </c:if>
</c:if>
</body>
</html>
