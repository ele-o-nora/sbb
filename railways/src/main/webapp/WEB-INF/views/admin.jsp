<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin panel</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h4 class="text-secondary m-4">Westerosi Railways admin panel</h4>
<div class="row">
    <div class="col-sm-6 offset-3">
        <div id="addStationMenu" class="bg-dark text-light m-3 rounded p-2">Add new station</div>
    </div>
</div>
<div class="row">
    <div class="col-sm-4 offset-4">
        <ul id="linesStationsList" class="list-group">
            <c:forEach var="line" items="${lines}">
                <li class="list-group-item">
                    <a href="${pageContext.request.contextPath}/admin/addStation/${line.id}"
                       class="text-danger">${line.name} line</a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<div class="row">
    <div class="col-sm-6 offset-3">
        <div id="modifyRouteMenu" class="bg-dark text-light m-3 rounded p-2">Add/modify routes</div>
    </div>
</div>
<div class="row">
    <div class="col-sm-4 offset-4">
        <ul id="linesRoutesList" class="list-group">
            <c:forEach var="line" items="${lines}">
                <li class="list-group-item">
                    <a href="#" id="${line.name}" class="text-danger">${line.name} line</a>
                    <ul class="list-group" id="routes${line.name}">
                        <c:forEach var="route" items="${routes}">
                            <c:if test="${route.line eq line.name}">
                                <li class="list-group-item">
                                    <a href="#" onclick="showRouteStations('#${route.id}')"
                                       class="text-secondary">${route.number}</a>
                                    <ul class="list-group m-3" style="display:none" id="${route.id}">
                                        <c:forEach var="stop" items="${route.stations}">
                                            <li class="list-group-item">
                                                <span class="font-weight-bold">${stop.name}</span>
                                                <c:if test="${!empty stop.waitTime}">
                                                    <br/>Time at station: ${stop.waitTime} minutes
                                                </c:if>
                                            </li>
                                        </c:forEach>
                                        <li class="list-group-item">
                                            <a href="${pageContext.request.contextPath}/admin/editRoute/${line.id}/${route.id}"
                                               class="text-secondary">Modify route</a>
                                        </li>
                                    </ul>
                                </li>
                            </c:if>
                        </c:forEach>
                        <li class="list-group-item">
                            <a href="${pageContext.request.contextPath}/admin/addRoute/${line.id}"
                               class="text-secondary">Add new route</a>
                        </li>
                    </ul>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<div class="row">
    <div class="col-sm-6 offset-3">
        <div id="scheduleRoutesMenu" class="bg-dark text-light m-3 rounded p-2">Schedule a route</div>
    </div>
</div>
<div class="row">
    <div class="col-sm-4 offset-4">
        <ul id="linesRoutesScheduleList" class="list-group">
            <c:forEach var="line" items="${lines}">
                <li class="list-group-item">
                    <a href="#" id="${line.name}Schedule" class="text-danger">${line.name} line</a>
                    <ul class="list-group" id="routes${line.name}Schedule">
                        <c:forEach var="route" items="${routes}">
                            <c:if test="${route.line eq line.name}">
                                <li class="list-group-item">
                                    <a href="#" onclick="showScheduleRoutesForm(${route.id})"
                                       class="text-secondary">${route.number}</a>
                                </li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<div class="row">
    <div class="col-sm-6 offset-3">
        <div class="bg-dark text-light m-3 rounded p-2">
            <a href="${pageContext.request.contextPath}/admin/journeys/${today}"
               class="text-light stretched-link">List all scheduled journeys</a>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-6 offset-3">
        <div id="trainModelsMenu" class="bg-dark text-light m-3 rounded p-2">Train models</div>
    </div>
</div>
<div class="row">
    <div class="col-sm-4 offset-4">
        <ul id="trainModelsList" class="list-group">
            <c:forEach var="train" items="${trainModels}">
                <li class="list-group-item">
                    <span class="text-secondary">Model: <span class="text-danger">${train.model}</span><br>
                        Seats: <span class="text-danger">${train.seats}</span>
                        Speed: <span class="text-danger">${train.speed}</span> mph</span>
                </li>
            </c:forEach>
            <li class="list-group-item active" id="addTrainMenu">
                <span class="text-secondary">Add new train model</span>
            </li>
        </ul>
    </div>
</div>
<div class="row">
    <div class="col-sm-6 offset-3">
        <div id="tariffMenu" class="bg-dark text-light m-3 rounded p-2">Update tariff</div>
    </div>
</div>
<div class="modal fade" id="updateTariffModal" tabindex="-1" role="dialog" aria-labelledby="updateTariffTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="updateTariffTitle">Update tariff</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/admin/updateTariff" method="post" id="updateTariffForm">
                    <div class="form-row justify-content-center m-3">
                        <div class="form-group col-sm-6">
                            <label for="price">Silver stags per ten leagues:</label>
                            <input type="number" name="price" value="${tariff}" autocomplete="off"
                                   class="form-control" step="0.01" id="price" required>
                        </div>
                    </div>
                    <div class="form-row justify-content-center m-3">
                        <div class="col-sm-2">
                            <input type="submit" value="Update" class="btn btn-outline-secondary float-right">
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="addTrainModal" tabindex="-1" role="dialog" aria-labelledby="trainAddTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="trainAddTitle">Add new train model</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/admin/addTrain" method="post" id="addTrainForm">
                    <div class="form-row justify-content-center m-3">
                        <div class="col-sm-6">
                            <input type="text" name="model" placeholder="Model name"
                                   autocomplete="off" class="form-control" required>
                        </div>
                        <div class="col-sm-3">
                            <input type="text" name="seats" placeholder="Seats" autocomplete="off"
                                   class="form-control" pattern="[1-9]{1}[0-9]{1,2}" required
                                   title="Please enter valid seats quantity"
                                   onkeyup="this.setCustomValidity(this.validity.patternMismatch ? this.title : '');">
                        </div>
                    </div>
                    <div class="form-row m-3">
                        <div class="col-sm-4 offset-3">
                            <input type="text" name="speed" placeholder="Speed (mph)" autocomplete="off"
                                   class="form-control" pattern="[1-9]{1}[0-9]{1,2}" required
                                   title="Please enter valid train speed"
                                   onkeyup="this.setCustomValidity(this.validity.patternMismatch ? this.title : '');">
                        </div>
                        <div class="col-sm-2">
                            <input type="submit" value="Add" class="btn btn-outline-secondary float-right">
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="scheduleRouteModal" tabindex="-1" role="dialog" aria-labelledby="scheduleRouteTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="scheduleRouteTitle">Schedule route</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/admin/scheduleRouteJourneys"
                      method="post" id="scheduleRouteForm">
                    <input type="hidden" name="routeId" id="routeId">
                    <div class="form-row justify-content-center m-3">
                        <div class='col-sm-6'>
                            <input type="text" class="form-control datetimepicker-input"
                                   id="datePickerFrom" data-toggle="datetimepicker" name="dateFrom"
                                   data-target="#datePickerFrom" placeholder="First day"/>
                        </div>
                        <div class='col-sm-6'>
                            <input type="text" class="form-control datetimepicker-input"
                                   id="datePickerUntil" data-toggle="datetimepicker" name="dateUntil"
                                   data-target="#datePickerUntil" placeholder="Last day"/>
                        </div>
                    </div>
                    <div class="form-row justify-content-center m-3">
                        <div class="col-sm-6">
                            <input type="text" class="form-control datetimepicker-input"
                                   id="timePicker" data-toggle="datetimepicker" name="departure"
                                   data-target="#timePicker" placeholder="Departure time"/>
                        </div>
                        <div class="col-sm-6">
                            <select name="trainId" class="custom-select">
                                <c:forEach items="${trainModels}" var="train">
                                    <option value="${train.id}">
                                            ${train.model}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-row justify-content-center m-3">
                        <div class="col-sm-4 form-check form-check-inline">
                            <input type="radio" name="direction" id="outbound" value="outbound"
                                   class="form-check-input" checked>
                            <label for="outbound" class="form-check-label">Outbound</label>
                        </div>
                        <div class="col-sm-4 form-check form-check-inline">
                            <input type="radio" name="direction" id="inbound" value="inbound" class="form-check-input">
                            <label for="inbound" class="form-check-label">Inbound</label>
                        </div>
                        <div class="col-sm-2">
                            <input type="submit" class="btn btn-outline-secondary" value="Schedule">
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
