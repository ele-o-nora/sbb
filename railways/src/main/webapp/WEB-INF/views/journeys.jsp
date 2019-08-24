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
<h5 class="text-secondary">Scheduled journeys for <span class="text-danger">${today}</span></h5>
<div class="row m-3 justify-content-center">
    <div class="col-sm-8">
        <a href="${pageContext.request.contextPath}/admin/journeys/${previousDay}"
           class="btn btn-outline-danger float-left">Previous day</a>
        <a href="${pageContext.request.contextPath}/admin/journeys/${nextDay}"
           class="btn btn-outline-danger float-right">Next day</a>
    </div>
</div>
<c:if test="${empty journeys}">
    <div class="m-5">
        There are no journeys scheduled for the day.
    </div>
</c:if>
<c:if test="${!empty journeys}">
    <div class="row justify-content-center">
        <div class="col-sm-8">
            <table class="table">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">Route</th>
                    <th scope="col">Direction</th>
                    <th scope="col">Departure</th>
                    <th scope="col">Train model</th>
                    <th scope="col">Status</th>
                    <th scope="col">Links</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="train" items="${journeys}">
                    <tr>
                        <th scope="row" class="align-middle">${train.route}</th>
                        <td class="align-middle">${train.destination}</td>
                        <td class="align-middle">
                            <c:forEach var="stop" items="${train.stops}" varStatus="status">
                                <c:if test="${status.first}">
                                    ${fn:substringAfter(stop.departure, " ")}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td class="align-middle">${train.trainType.model}</td>
                        <td class="align-middle">
                            <c:choose>
                                <c:when test="${train.status eq 'Cancelled'}">
                                    <span class="text-danger">${train.status}</span>
                                </c:when>
                                <c:otherwise>
                                    ${train.status}<br/>
                                    <c:if test="${train.enRoute}">
                                    <a href="#" onclick="showAdjustJourneyModal(${train.id})" class="text-danger">Adjust</a>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="align-middle">
                            <a href="${pageContext.request.contextPath}/admin/journey/${train.id}" class="text-danger">
                                Detailed schedule</a><br/>
                            <a href="${pageContext.request.contextPath}/admin/journey/${train.id}/passengers"
                               class="text-danger">Passenger list</a>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td class="align-middle" colspan="5">
                        <c:if test="${!empty previousPage}">
                            <a href="${pageContext.request.contextPath}/admin/journeys/${today}?page=${previousPage}"
                               class="btn btn-outline-dark float-left">Previous 10</a>
                        </c:if>
                    </td>
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
</c:if>
<div class="modal fade" id="adjustJourneyModal" tabindex="-1" role="dialog" aria-labelledby="adjustJourneyTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="adjustJourneyTitle">Adjust journey</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-6 border-right">
                        <form action="${pageContext.request.contextPath}/admin/delay" method="post">
                            <input type="hidden" name="journeyId" id="delayId" value="0">
                            <input type="hidden" name="date" value="${today}">
                            <div class="form-row justify-content-center mt-1 mb-3">
                                <div class="col-sm-10">
                                <input type="number" name="delay" class="form-control" placeholder="Minutes delayed">
                                </div>
                            </div>
                            <div class="form-row justify-content-center">
                                <input type="submit" value="Set delay" class="btn btn-outline-secondary">
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>
                    </div>
                    <div class="col-sm-6">
                        <form action="${pageContext.request.contextPath}/admin/cancel" method="post">
                            <input type="hidden" name="journeyId" id="cancelId" value="0">
                            <input type="hidden" name="date" value="${today}">
                            <div class="form-row justify-content-center">
                                <label>Warning: this action is irreversible</label>
                            </div>
                            <div class="form-row justify-content-center">
                                <input type="submit" value="Cancel journey" class="btn btn-outline-danger">
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
