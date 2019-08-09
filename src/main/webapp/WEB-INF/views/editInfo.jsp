<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
        <form:form action="${pageContext.request.contextPath}/changeName" method="post"
                   modelAttribute="changeNameDto">
            <form:errors path = "*" cssClass = "text-danger" element = "div" />
            <div class="form-row justify-content-center m-3">
                <div class="col-sm-8">
                    <form:input type="text" class="form-control" path="firstName" value="${passenger.firstName}"
                                required="required" />
                </div>
            </div>
            <div class="form-row justify-content-center m-3">
                <div class="col-sm-8">
                    <form:input type="text" class="form-control" path="lastName" value="${passenger.lastName}"
                                required="required" />
                </div>
            </div>
            <div class="form-row justify-content-center m-3">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" class="btn btn-outline-secondary" value="Update">
            </div>
        </form:form>
    </div>
    <div class="col-sm-6">
        <h5 class="text-secondary">Change password</h5>
        <form:form action="${pageContext.request.contextPath}/changePassword" method="post"
                   modelAttribute="passwordDto">
            <form:errors path = "*" cssClass = "text-danger" element = "div" />
            <div class="form-row justify-content-center m-3">
                <div class="col-sm-8">
                    <form:input type="password" path="password" placeholder="Password" class="form-control" id="newPassword"
                           pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}" title="Password should be at least 6 symbols long,
                    with at least one number, one lowercase and one uppercase letter" onkeyup="
                    this.setCustomValidity(this.validity.patternMismatch ? this.title : '');
                    form.matchingPassword.pattern = RegExp.escape(this.value);" required="required"/>
                </div>
            </div>
            <div class="form-row justify-content-center m-3">
                <div class="col-sm-8">
                    <form:input type="password" path="matchingPassword" placeholder="Confirm password" id="confirmNewPassword"
                           class="form-control" title="Password should match the first one" required="required"
                                onkeyup="this.setCustomValidity(this.validity.patternMismatch ? this.title : '');"/>
                </div>
            </div>
            <div class="form-row justify-content-center m-3">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" class="btn btn-outline-secondary" value="Change password">
            </div>
        </form:form>
    </div>
</div>
</body>
</html>
