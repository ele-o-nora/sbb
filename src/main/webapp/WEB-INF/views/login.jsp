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
    <div class="row justify-content-center">
    <h5 class="col-sm-4 bg-danger text-light m-5 p-3 rounded">Bad credentials!</h5>
    </div>
</c:if>
<h2 class="text-muted">Please sign in</h2>
<form action="${pageContext.request.contextPath}/perform_login" method="POST">
    <div class="form-row m-3">
        <div class="col-sm-3 offset-sm-3">
            <input type="text" class="form-control" name="username"
                   placeholder="E-mail" />
        </div>
        <div class="col-sm-3">
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
