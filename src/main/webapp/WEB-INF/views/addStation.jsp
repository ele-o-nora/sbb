<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add new station</title>
</head>
<body>
<%@include file="header.jsp" %>
<c:if test="${!empty lines}">
    <h3>Choose line:</h3>
    <ul>
        <c:forEach var="line" items="${lines}">
            <li><a href="${pageContext.request.contextPath}/admin/addStation/${line.id}">${line.name}</a></li>
        </c:forEach>
    </ul>
</c:if>
<c:if test="${!empty stations}">
    <h3>Add new station to line: ${line.name}</h3>
    <c:forEach var="station" items="${stations}" varStatus="status">
        <c:if test="${status.first}">Zone: ${station.zone}<br/>
        <input type="button" value="+"><br/></c:if>
        ${station.name}<br/>
        <input type="button" value="+"><br/>
        <c:if test="${stations[status.index+1].zone gt station.zone}">
            <c:forEach begin="${station.zone+1}" end="${stations[status.index+1].zone}" varStatus="loop">
                Zone: ${loop.index}<br/>
                <input type="button" value="+"><br/>
            </c:forEach>
        </c:if>
        <c:if test="${status.last}">
            <c:forEach begin="${station.zone+1}" end="7" varStatus="loop">
                Zone: ${loop.index}<br/>
                <input type="button" value="+"><br/>
            </c:forEach>
        </c:if>
    </c:forEach>
</c:if>
</body>
</html>
