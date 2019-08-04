<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Buy tickets</title>
</head>
<body>
<%@include file="header.jsp" %>
<form <c:if test="${!empty ticketOrder}">action="${pageContext.request.contextPath}/finalizeTicketSale"</c:if>
        <c:if test="${!empty transferTickets}">action="${pageContext.request.contextPath}/finalizeTicketsSale"</c:if>
      method="post">
    <c:if test="${empty passenger}">
        <div class="form-row justify-content-center">
            <div class="col-sm-6">
                <input type="text" name="firstName" placeholder="First name" class="form-control" required>
            </div>
            <div class="col-sm-6">
                <input type="text" name="lastName" placeholder="Last name" class="form-control" required>
            </div>
        </div>
        <div class="form-row justify-content-center">
            <div class="col-sm-6">
                <input type="text" name="dateOfBirth" placeholder="Date of birth"
                       class="form-control datetimepicker-input" id="dateOfBirthPicker"
                       data-toggle="datetimepicker" data-target="#dateOfBirthPicker" required>
            </div>
        </div>
    </c:if>
    <c:if test="${!empty passenger}">
        <input type="hidden" name="email" value="<sec:authentication property="principal.username" />">
        <div class="row justify-content-center">
            <div class="col-sm-6">
                First name: <span class="text-danger">${passenger.firstName}</span>
            </div>
            <div class="col-sm-6">
                Last name: <span class="text-danger">${passenger.lastName}</span>
            </div>
        </div>
        <div class="row justify-content-center">
            Date of birth: <span class="text-danger">${passenger.dateOfBirth}</span>
        </div>
    </c:if>

    <div class="form-row justify-content-center">
        <input type="submit" class="btn btn-outline-secondary">
    </div>
</form>
</body>
</html>
