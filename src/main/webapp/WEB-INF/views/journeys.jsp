<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Scheduled journeys</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h5 class="text-secondary">Scheduled journeys for ${today}</h5>
<div class="row m-3 justify-content-center">
    <div class="col-sm-8">
        <c:if test="${!empty previousDay}">
            <a href="${pageContext.request.contextPath}/admin/journeys/${previousDay}"
               class="btn btn-outline-danger float-left">Previous day</a>
        </c:if>
        <a href="${pageContext.request.contextPath}/admin/journeys/${nextDay}"
           class="btn btn-outline-danger float-right">Next day</a>
    </div>
</div>
<div class="row justify-content-center">
    <div class="col-sm-8">
        <table class="table">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Route</th>
                <th scope="col">Direction</th>
                <th scope="col">Departure</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="train" items="${journeys}">
                <tr>
                    <td class="align-middle">${train.route}</td>
                    <td class="align-middle">${train.destination}</td>
                    <td class="align-middle">
                        <c:forEach var="stop" items="${train.stops}" varStatus="status">
                            <c:if test="${status.first}">
                                ${fn:substringAfter(stop.departure, " ")}
                            </c:if>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td class="align-middle">
                    <c:if test="${!empty previousPage}">
                        <a href="${pageContext.request.contextPath}/admin/journeys/${today}?page=${previousPage}"
                           class="btn btn-outline-dark float-left">Previous 10</a>
                    </c:if>
                </td>
                <td class="align-middle"></td>
                <td class="align-middle">
                    <c:if test="${!empty nextPage}">
                        <a href="${pageContext.request.contextPath}/admin/journeys/${today}?page=${nextPage}"
                           class="btn btn-outline-dark float-right">Next 10</a>
                    </c:if>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
