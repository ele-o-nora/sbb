<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My tickets</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<c:if test="${!empty status}">
    <p class="text-danger m-5">${status}</p>
</c:if>
<h5 class="text-secondary">My tickets</h5>
<c:if test="${empty tickets}">
    <div class="m-5">
        <span>You have not bought any tickets yet.</span><br/>
        <a href="${pageContext.request.contextPath}/" class="text-danger">Search for a journey</a>
    </div>
</c:if>
<c:if test="${!empty tickets}">
<div class="row justify-content-center">
    <div class="col-sm-8">
        <c:forEach items="${tickets}" var="ticket">
            <div class="row m-2 justify-content-center">
                <c:if test="${ticket.category ne 'old'}">
                    <div class="border border-dark p-2 col-sm-10">
                        <c:if test="${ticket.journey.status eq 'Cancelled'}">
                            <div class="row mt-2 justify-content-center">
                                <span class="font-weight-bold text-danger">Cancelled</span>
                            </div>
                        </c:if>
                        <div class="row mt-2 justify-content-center">
                            <span class="font-weight-bold mr-1">Route: </span> ${ticket.journey.route}
                        </div>
                        <div class="row mb-2 justify-content-center">
                            <span class="font-weight-bold mr-1"> Direction: </span> ${ticket.journey.destination}
                        </div>
                        <div class="row m-2 p-2 border-top border-bottom">
                            <div class="col-sm-6">
                                <div class="row justify-content-center">
                                    <span class="font-weight-bold mr-1">From: </span> ${ticket.stationFrom.station}
                                </div>
                                <div class="row justify-content-center">
                                    <span class="font-weight-bold mr-1"> Departure: </span> ${ticket.stationFrom.departure}
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="row justify-content-center">
                                    <span class="font-weight-bold mr-1">To: </span> ${ticket.stationTo.station}
                                </div>
                                <div class="row justify-content-center">
                                    <span class="font-weight-bold mr-1"> Arrival: </span> ${ticket.stationTo.arrival}
                                </div>
                            </div>
                        </div>
                        <div class="row m-2 justify-content-center">
                            <span class="font-weight-bold mr-1">Price: </span> ${ticket.formattedPrice}
                        </div>
                        <c:if test="${ticket.category eq 'future'}">
                            <div class="row m-2 justify-content-center">
                                <form action="${pageContext.request.contextPath}/returnTicket" method="post">
                                    <input type="hidden" value="${ticket.id}" name="ticketId">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="submit" value="Return ticket" class="btn btn-outline-danger">
                                </form>
                            </div>
                        </c:if>
                    </div>
                </c:if>
                <c:if test="${ticket.category eq 'old'}">
                    <div class="border text-muted p-2 col-sm-10">
                        <c:if test="${ticket.journey.status eq 'Cancelled'}">
                            <div class="row mt-2 justify-content-center">
                                <span class="font-weight-bold">Cancelled</span>
                            </div>
                        </c:if>
                        <div class="row mt-2 justify-content-center">
                            <span class="font-weight-bold mr-1">Route: </span> ${ticket.journey.route}
                        </div>
                        <div class="row mb-2 justify-content-center">
                            <span class="font-weight-bold mr-1">Direction: </span> ${ticket.journey.destination}
                        </div>
                        <div class="row m-2 p-2 border-top border-bottom">
                            <div class="col-sm-6">
                                <div class="row justify-content-center">
                                    <span class="font-weight-bold mr-1">From: </span> ${ticket.stationFrom.station}
                                </div>
                                <div class="row justify-content-center">
                                    <span class="font-weight-bold mr-1">Departure: </span> ${ticket.stationFrom.departure}
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="row justify-content-center">
                                    <span class="font-weight-bold mr-1">To: </span> ${ticket.stationTo.station}
                                </div>
                                <div class="row justify-content-center">
                                    <span class="font-weight-bold mr-1">Arrival: </span> ${ticket.stationTo.arrival}
                                </div>
                            </div>
                        </div>
                        <div class="row m-2 justify-content-center">
                            <span class="font-weight-bold mr-1 ml-1">Price: </span> ${ticket.formattedPrice}
                        </div>
                    </div>
                </c:if>
            </div>
        </c:forEach>
        <div class="row m-3">
            <div class="col">
                <c:if test="${!empty previousPage}">
                    <a href="${pageContext.request.contextPath}/myTickets?page=${previousPage}"
                       class="btn btn-outline-dark float-left">Previous page</a>
                </c:if>
            </div>
            <div class="col">
                <c:if test="${!empty nextPage}">
                    <a href="${pageContext.request.contextPath}/myTickets?page=${nextPage}"
                       class="btn btn-outline-dark float-right">Next page</a>
                </c:if>
            </div>
        </div>
    </div>
</div>
</c:if>
</body>
</html>
