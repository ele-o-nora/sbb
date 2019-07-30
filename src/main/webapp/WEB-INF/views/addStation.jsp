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
<h3>Add new station to line: ${line.name}</h3>
<form action="${pageContext.request.contextPath}/admin/addNewStation" method="post" id="addStationForm">
    <input type="hidden" name="zone" id="zone" value="0">
    <input type="hidden" name="order" id="order" value="0">
    <input type="hidden" name="line" value="${line.id}">
    <input type="text" placeholder="Station name" name="name" id="addStationName" required>
    <input type="submit" value="Add station">
</form>
<c:forEach var="station" items="${stations}" varStatus="status">
<c:if test="${status.first}">Zone: ${station.zone}<br/>
<input type="button" value="+" onclick="toggleForm(${station.zone}, 1)"><br/></c:if>
    ${station.name}<br/>
<input type="button" value="+" onclick="toggleForm(${station.zone}, ${status.index+2})"><br/>
<c:if test="${stations[status.index+1].zone gt station.zone}">
<c:forEach begin="${station.zone+1}" end="${stations[status.index+1].zone}" varStatus="loop">
Zone: ${loop.index}<br/>
<input type="button" value="+" onclick="toggleForm(${loop.index}, ${status.index+2})"><br/>
</c:forEach>
</c:if>
<c:if test="${status.last}">
<c:forEach begin="${station.zone+1}" end="7" varStatus="loop">
Zone: ${loop.index}<br/>
<input type="button" value="+" onclick="toggleForm(${loop.index}, ${status.index+2})"><br/>
</c:forEach>
</c:if>
</c:forEach>
</body>
</html>
