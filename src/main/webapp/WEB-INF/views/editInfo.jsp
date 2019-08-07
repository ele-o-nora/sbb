<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit account details</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<c:if test="${!empty status}">
    <p class="text-danger m-5">${status}</p>
</c:if>
<div class="row justify-content-center">
    <div class="col-sm-6">
        <h5 class="text-secondary">Change name/surname</h5>
        <form action="${pageContext.request.contextPath}/changeName" method="post">
            <div class="form-row justify-content-center m-3">
                <div class="col-sm-8">
                    <input type="text" class="form-control" name="firstName" value="${passenger.firstName}" required>
                </div>
            </div>
            <div class="form-row justify-content-center m-3">
                <div class="col-sm-8">
                    <input type="text" class="form-control" name="lastName" value="${passenger.lastName}" required>
                </div>
            </div>
            <div class="form-row justify-content-center m-3">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" class="btn btn-outline-secondary" value="Update">
            </div>
        </form>
    </div>
    <div class="col-sm-6">
        <h5 class="text-secondary">Change password</h5>
        <form action="${pageContext.request.contextPath}/changePassword" method="post">
            <div class="form-row justify-content-center m-3">
                <div class="col-sm-8">
                    <input type="password" name="password" placeholder="Password" class="form-control" id="newPassword"
                           pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}" title="Password should be at least 6 symbols long,
                    with at least one number, one lowercase and one uppercase letter" onkeyup="
                    this.setCustomValidity(this.validity.patternMismatch ? this.title : '');
                    form.matchingPassword.pattern = RegExp.escape(this.value);" required>
                </div>
            </div>
            <div class="form-row justify-content-center m-3">
                <div class="col-sm-8">
                    <input type="password" name="matchingPassword" placeholder="Confirm password" id="confirmNewPassword"
                           class="form-control" title="Password should match the first one" required onkeyup="
                            this.setCustomValidity(this.validity.patternMismatch ? this.title : '');">
                </div>
            </div>
            <div class="form-row justify-content-center m-3">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" class="btn btn-outline-secondary" value="Change password">
            </div>
        </form>
    </div>
</div>
</body>
</html>
