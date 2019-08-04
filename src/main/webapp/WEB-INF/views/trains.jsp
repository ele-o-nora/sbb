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
<body class="text-center">
<%@include file="header.jsp" %>
<c:if test="${!empty error}">
    <div class="m-5">
        <span class="text-danger">${error}</span><br/>
        <a href="${pageContext.request.contextPath}/">Search anew</a>
    </div>
</c:if>
<c:if test="${empty error}">
    <h4 class="m-4">Trains from <span class="text-danger">${origin}</span>
        to <span class="text-danger">${destination}</span></h4>
    <div class="row">
        <div class="col-sm-8 offset-sm-2">
            <c:if test="${!empty fail}">
                ${fail}<br/>
                <a href="${pageContext.request.contextPath}/">Search anew</a>
            </c:if>
            <table class="table">
                <c:if test="${!empty trains}">
                    <thead class="thead-dark">
                    <tr>
                        <th scope="col">Route</th>
                        <th scope="col">Direction</th>
                        <th scope="col">Departs from ${origin}</th>
                        <th scope="col">Arrives at ${destination}</th>
                        <th scope="col">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="train" items="${trains}">
                        <tr>
                            <td class="align-middle">${train.route}</td>
                            <td class="align-middle">${train.destination}</td>
                            <td class="align-middle"><c:forEach var="stop" items="${train.stops}">
                                <c:if test="${stop.station eq origin}">
                                    ${stop.departure}
                                </c:if>
                            </c:forEach></td>
                            <td class="align-middle"><c:forEach var="stop" items="${train.stops}">
                                <c:if test="${stop.station eq destination}">
                                    ${stop.arrival}
                                </c:if>
                            </c:forEach></td>
                            <td class="align-middle">
                                <form action="${pageContext.request.contextPath}/buyTicket" method="post">
                                    <input type="hidden" name="journeyId" value="${train.id}">
                                    <input type="hidden" name="from"
                                           value="${origin}">
                                    <input type="hidden" name="to"
                                           value="${destination}">
                                    <input type="submit" class="btn btn-outline-danger" value="Buy ticket"/>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </c:if>
                <c:if test="${!empty connections}">
                    <thead class="thead-dark">
                    <tr>
                        <th scope="col">Route</th>
                        <th scope="col">Direction</th>
                        <th scope="col">Departs from ${origin}</th>
                        <th scope="col">Transfer</th>
                        <th scope="col">Arrives at ${destination}</th>
                        <th scope="col">Action</th>
                    </tr>
                    <c:forEach var="connection" items="${connections}">
                        <tbody>
                        <tr>
                            <td class="align-middle">${connection.firstTrain.route}</td>
                            <td class="align-middle">${connection.firstTrain.destination}</td>
                            <td class="align-middle"><c:forEach var="stop" items="${connection.firstTrain.stops}">
                                <c:if test="${stop.station eq origin}">
                                    ${stop.departure}
                                </c:if>
                            </c:forEach></td>
                            <td class="align-middle"><c:forEach var="stop" items="${connection.firstTrain.stops}">
                                <c:if test="${stop.station eq connection.transferStation}">
                                    <span class="font-weight-bold">${connection.transferStation}</span>
                                    <br/>${stop.arrival}
                                </c:if>
                            </c:forEach></td>
                            <td class="align-middle">-</td>
                            <td rowspan="2" class="align-middle">
                                <form action="${pageContext.request.contextPath}/buyTickets" method="post">
                                    <input type="hidden" name="firstJourneyId" value="${connection.firstTrain.id}">
                                    <input type="hidden" name="secondJourneyId" value="${connection.secondTrain.id}">
                                    <input type="hidden" name="from" value="${origin}">
                                    <input type="hidden" name="transfer" value="${connection.transferStation}">
                                    <input type="hidden" name="to" value="${destination}">
                                <input type="submit" class="btn btn-outline-danger" value="Buy tickets"/>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td class="align-middle">${connection.secondTrain.route}</td>
                            <td class="align-middle">${connection.secondTrain.destination}</td>
                            <td class="align-middle">-</td>
                            <td class="align-middle"><c:forEach var="stop" items="${connection.secondTrain.stops}">
                                <c:if test="${stop.station eq connection.transferStation}">
                                    ${stop.departure}
                                </c:if>
                            </c:forEach></td>
                            <td class="align-middle"><c:forEach var="stop" items="${connection.secondTrain.stops}">
                                <c:if test="${stop.station eq destination}">
                                    ${stop.arrival}
                                </c:if>
                            </c:forEach></td>
                        </tr>
                        </tbody>
                    </c:forEach>
                    </thead>
                </c:if>
            </table>
        </div>
    </div>
</c:if>
</body>
</html>
