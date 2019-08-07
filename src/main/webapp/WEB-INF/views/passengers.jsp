<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Passengers</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h5 class="text-secondary m-4">List of passengers for ${journey.route} direction ${journey.destination},
    departing <c:forEach var="stop" items="${journey.stops}" varStatus="status">
        <c:if test="${status.first}">
            ${stop.departure}
        </c:if>
    </c:forEach></h5>
<c:if test="${empty tickets}">
    <div class="m-5">
        There are no tickets bought for the journey.
    </div>
</c:if>
<c:if test="${!empty tickets}">
    <div class="row justify-content-center">
        <div class="col-sm-8">
            <table class="table">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">First name</th>
                    <th scope="col">Last name</th>
                    <th scope="col">Date of birth</th>
                    <th scope="col">Travels from</th>
                    <th scope="col">Travels to</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="ticket" items="${tickets}">
                    <tr>
                        <td class="align-middle">${ticket.passenger.firstName}</td>
                        <td class="align-middle">${ticket.passenger.lastName}</td>
                        <td class="align-middle">${ticket.passenger.dateOfBirth}</td>
                        <td class="align-middle">${ticket.stationFrom.station}</td>
                        <td class="align-middle">${ticket.stationTo.station}</td>
                    </tr>
                </c:forEach>
                <tr>
                    <td class="align-middle" colspan="4">
                        <c:if test="${!empty previousPage}">
                            <a href="${pageContext.request.contextPath}/admin/journey/${journey.id}/passengers?page=${previousPage}"
                               class="btn btn-outline-dark float-left">Previous 10</a>
                        </c:if>
                    </td>
                    <td class="align-middle">
                        <c:if test="${!empty nextPage}">
                            <a href="${pageContext.request.contextPath}/admin/journey/${journey.id}/passengers?page=${nextPage}"
                               class="btn btn-outline-dark float-right">Next 10</a>
                        </c:if>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</c:if>
</body>
</html>
