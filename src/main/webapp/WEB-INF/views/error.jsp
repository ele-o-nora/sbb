<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Oh no</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h2>Something went wrong :(</h2>
<a href="${pageContext.request.contextPath}/" class="text-danger">Back to main page</a>
</body>
</html>
