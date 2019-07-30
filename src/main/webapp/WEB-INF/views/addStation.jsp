<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add new station</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h4 class="text-secondary m-4">Add new station to ${line.name} line</h4>
<form action="${pageContext.request.contextPath}/admin/addNewStation" method="post" id="addStationForm">
    <input type="hidden" name="zone" id="zone" value="0">
    <input type="hidden" name="order" id="order" value="0">
    <input type="hidden" name="line" value="${line.id}">
    <div class="form-row">
        <div class="col-sm-3 offset-4">
            <input type="text" placeholder="Station name" name="name"
                   id="addStationName" class="form-control" required>
        </div>
        <div class="col-sm-1">
            <input type="submit" value="Add station" class="btn btn-outline-secondary">
        </div>
    </div>
</form>
<div class="row">
    <div class="col-sm-6 offset-3">
        <c:forEach var="station" items="${stations}" varStatus="status">
            <c:if test="${status.first}">
                <div class="m-3 p-3">
                <span class="font-weight-bold">Zone: ${station.zone} </span><br/>
                <input type="button" value="+" onclick="showAddStationForm(${station.zone}, 1)"
                       class="btn btn-outline-dark rounded-circle m-2"><br/></c:if>
            <span class="text-secondary">${station.name}</span><br/>
            <input type="button" value="+" onclick="showAddStationForm(${station.zone}, ${status.index+2})"
                   class="btn btn-outline-dark rounded-circle m-2"><br/>
            <c:if test="${stations[status.index+1].zone gt station.zone}">
                <c:forEach begin="${station.zone+1}" end="${stations[status.index+1].zone}" varStatus="loop">
                    </div>
                    <div class="border-top border-secondary m-3 p-3">
                    <span class="font-weight-bold">Zone: ${loop.index}</span><br/>
                    <input type="button" value="+" onclick="showAddStationForm(${loop.index}, ${status.index+2})"
                           class="btn btn-outline-dark rounded-circle m-2"><br/>
                </c:forEach>
            </c:if>
            <c:if test="${status.last}">
                <c:forEach begin="${station.zone+1}" end="7" varStatus="loop">
                    </div>
                    <div class="border-top border-secondary m-3 p-3">
                    <span class="font-weight-bold">Zone: ${loop.index}</span><br/>
                    <input type="button" value="+" onclick="showAddStationForm(${loop.index}, ${status.index+2})"
                           class="btn btn-outline-dark rounded-circle m-2"><br/>
                </c:forEach>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>
</body>
</html>
