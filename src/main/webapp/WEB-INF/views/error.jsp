<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Something went wrong</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h3>Something went wrong</h3>
<img src="${pageContext.request.contextPath}/resources/doom.jpg" alt="Doom of Valyria"/>
<blockquote class="blockquote">
    <p class="mb-0">Valar morghulis was how they said it in Valyria of old.
        All men must die. And the Doom came and proved it true.</p>
    <footer class="blockquote-footer">
        Oberyn Martell in <cite>A Storm of Swords, Tyrion IX</cite>
    </footer>
</blockquote>
<a href="${pageContext.request.contextPath}/" class="text-danger">Back to main page</a>
</body>
</html>
