<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Access denied</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h3>Access denied</h3>
<img src="${pageContext.request.contextPath}/resources/wall.jpg" alt="The Wall and Castle Black by Feliche"/>
<div class="row justify-content-center">
<blockquote class="blockquote col-sm-10">
    <p class="mb-0">The Wall was too big to be stormed by any conventional means;
        too high for ladders or siege towers, too thick for battering rams.
        No catapult could throw a stone large enough to breach it, and if you tried to set it on fire,
        the icemelt would quench the flames.</p>
    <footer class="blockquote-footer">
        <cite>A Storm of Swords, Jon VIII</cite>
    </footer>
</blockquote>
</div>
<a href="${pageContext.request.contextPath}/" class="text-danger">Back to main page</a>
</body>
</html>
