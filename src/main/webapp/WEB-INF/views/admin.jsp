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
        <ul id="linesStationsList" class="list-group">
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
        <div id="modifyRouteMenu" class="bg-dark text-light m-3 rounded">Add/modify routes</div>
    </div>
</div>
<div class="row">
    <div class="col-sm-4 offset-4">
        <ul id="linesRoutesList" class="list-group">
            <c:forEach var="line" items="${lines}">
                <li id="${line.name}" class="list-group-item">
                    <span class="text-danger">${line.name} line</span>
                    <ul class="list-group" id="routes${line.name}">
                        <c:forEach var="route" items="${routes}">
                            <c:if test="${route.line eq line.name}">
                                <li class="list-group-item">
                                    <a href="${pageContext.request.contextPath}/admin/editRoute/${line.id}/${route.id}"
                                       class="text-secondary stretched-link">${route.number}</a>
                                </li>
                            </c:if>
                        </c:forEach>
                        <li class="list-group-item">
                            <a href="${pageContext.request.contextPath}/admin/addRoute/${line.id}"
                               class="text-secondary stretched-link">Add new route</a>
                        </li>
                    </ul>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<div class="row">
    <div class="col-sm-6 offset-3">
        <div id="trainModelsMenu" class="bg-dark text-light m-3 rounded">Train models</div>
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
<div class="modal fade" id="addTrainModal" tabindex="-1" role="dialog" aria-labelledby="trainAddTitle" aria-hidden="true">
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
                                   class="form-control" required>
                        </div>
                    </div>
                    <div class="form-row m-3">
                        <div class="col-sm-4 offset-3">
                            <input type="text" name="speed" placeholder="Speed (mph)" autocomplete="off"
                                   class="form-control" required>
                        </div>
                        <div class="col-sm-2">
                            <input type="submit" value="Add" class="btn btn-outline-secondary float-right">
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
