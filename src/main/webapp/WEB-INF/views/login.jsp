<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign in</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<c:if test="${!empty param.error}">
    <p class="text-danger m-5">Bad credentials!</p>
</c:if>
<h2 class="text-muted">Please sign in</h2>
<form action="${pageContext.request.contextPath}/perform_login" method="POST">
    <div class="form-row">
        <div class="col-sm-4 offset-sm-2">
            <input type="text" class="form-control" name="username"
                   placeholder="Username" />
        </div>
        <div class="col-sm-4">
            <input type="password" class="form-control" name="password"
                   placeholder="Password" />
        </div>
    </div>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="submit" value="Submit"
           class="btn btn-outline-secondary m-1" />
</form>
</body>
</html>
