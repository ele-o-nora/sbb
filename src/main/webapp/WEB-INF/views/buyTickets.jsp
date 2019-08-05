<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Buy tickets</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<c:if test="${!empty status}">
    <span class="text-danger m-5">${status}</span>
    <a href="${pageContext.request.contextPath}/">Back to main page</a>
</c:if>
<c:if test="${empty status}"><h5 class="text-secondary m-3">Passenger details:</h5></c:if>
<c:if test="${!empty ticketOrder}">
    <form:form modelAttribute="ticketOrder" method="post"
               action="${pageContext.request.contextPath}/finalizeTicketSale" autocomplete="off">
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-4">
                <input type="text" class="form-control" name="firstName" placeholder="First name"
                       <c:if test="${!empty passenger}">value="${passenger.firstName}"</c:if> required/>
            </div>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="lastName" placeholder="Last name"
                       <c:if test="${!empty passenger}">value="${passenger.lastName}"</c:if> required/>
            </div>
        </div>
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-4">
                <input type="text" name="dateOfBirth" placeholder="Date of birth"
                       class="form-control datetimepicker-input" id="dateOfBirthPicker2"
                       data-toggle="datetimepicker" data-target="#dateOfBirthPicker2" required/>
            </div>
        </div>
        <h5 class="text-secondary m-3">Travel info:</h5>
        <div class="row justify-content-center m-2">
            <div class="col-sm-6 m-1 border">
                Route: <span class="text-danger m-1">${ticketOrder.journey.route}</span>
                Direction: <span class="text-danger m-1">${ticketOrder.journey.destination}</span> <br/>
                From: <span class="text-danger m-1">${ticketOrder.origin.station}</span>
                Departs: <span class="text-danger m-1">${ticketOrder.origin.departure}</span><br/>
                To: <span class="text-danger m-1">${ticketOrder.destination.station}</span>
                Arrives: <span class="text-danger m-1">${ticketOrder.destination.arrival}</span><br/>
                Price: <span class="text-danger m-1">${ticketOrder.formattedPrice}</span>
                <form:input type="hidden" path="journey.id" value="${ticketOrder.journey.id}"/>
                <form:input type="hidden" path="origin.id" value="${ticketOrder.origin.id}"/>
                <form:input type="hidden" path="destination.id" value="${ticketOrder.destination.id}"/>
                <form:input type="hidden" path="price" value="${ticketOrder.price}"/>
            </div>
        </div>
        <div class="form-row justify-content-center m-2">
            <input type="submit" class="btn btn-outline-danger" value="Buy ticket"/>
        </div>
    </form:form>
</c:if>
<c:if test="${!empty transferTickets}">
    <form:form modelAttribute="transferTickets" method="post"
               action="${pageContext.request.contextPath}/finalizeTicketsSale" autocomplete="off">
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-4">
                <input type="text" class="form-control" name="firstName" placeholder="First name"
                       <c:if test="${!empty passenger}">value="${passenger.firstName}"</c:if> required/>
            </div>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="lastName" placeholder="Last name"
                       <c:if test="${!empty passenger}">value="${passenger.lastName}"</c:if> required/>
            </div>
        </div>
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-4">
                <input type="text" name="dateOfBirth" placeholder="Date of birth"
                       class="form-control datetimepicker-input" id="dateOfBirthPicker2"
                       data-toggle="datetimepicker" data-target="#dateOfBirthPicker2" required/>
            </div>
        </div>
        <h5 class="text-secondary m-3">Travel info:</h5>
        <div class="row justify-content-center m-2">
            <div class="col-sm-5 m-1 border">
                Route: <span class="text-danger m-1">${transferTickets.firstTrain.journey.route}</span>
                Direction: <span class="text-danger m-1">${transferTickets.firstTrain.journey.destination}</span> <br/>
                From: <span class="text-danger m-1">${transferTickets.firstTrain.origin.station}</span>
                Departs: <span class="text-danger m-1">${transferTickets.firstTrain.origin.departure}</span><br/>
                To: <span class="text-danger m-1">${transferTickets.firstTrain.destination.station}</span>
                Arrives: <span class="text-danger m-1">${transferTickets.firstTrain.destination.arrival}</span><br/>
                Price: <span class="text-danger m-1">${transferTickets.firstTrain.formattedPrice}</span>
                <form:input type="hidden" path="firstTrain.journey.id"
                            value="${transferTickets.firstTrain.journey.id}"/>
                <form:input type="hidden" path="firstTrain.origin.id" value="${transferTickets.firstTrain.origin.id}"/>
                <form:input type="hidden" path="firstTrain.destination.id"
                            value="${transferTickets.firstTrain.destination.id}"/>
                <form:input type="hidden" path="firstTrain.price" value="${transferTickets.firstTrain.price}"/>
            </div>
            <div class="col-sm-5 m-1 border">
                Route: <span class="text-danger m-1">${transferTickets.secondTrain.journey.route}</span>
                Direction: <span class="text-danger m-1">${transferTickets.secondTrain.journey.destination}</span> <br/>
                From: <span class="text-danger m-1">${transferTickets.secondTrain.origin.station}</span>
                Departs: <span class="text-danger m-1">${transferTickets.secondTrain.origin.departure}</span><br/>
                To: <span class="text-danger m-1">${transferTickets.secondTrain.destination.station}</span>
                Arrives: <span class="text-danger m-1">${transferTickets.secondTrain.destination.arrival}</span><br/>
                Price: <span class="text-danger m-1">${transferTickets.secondTrain.formattedPrice}</span>
                <form:input type="hidden" path="secondTrain.journey.id"
                            value="${transferTickets.secondTrain.journey.id}"/>
                <form:input type="hidden" path="secondTrain.origin.id"
                            value="${transferTickets.secondTrain.origin.id}"/>
                <form:input type="hidden" path="secondTrain.destination.id"
                            value="${transferTickets.secondTrain.destination.id}"/>
                <form:input type="hidden" path="secondTrain.price" value="${transferTickets.secondTrain.price}"/>
            </div>
        </div>
        <div class="form-row justify-content-center m-2">
            <input type="submit" value="Buy tickets" class="btn btn-outline-danger">
        </div>
    </form:form>
</c:if>

<c:if test="${!empty passenger}">
    <script type="text/javascript">
        function pasteDate() {
            $('#dateOfBirthPicker2').datetimepicker('date', moment('${passenger.dateOfBirth}', 'YYYY-MM-DD'));
        }
        window.onload = setTimeout("pasteDate()", 10);
    </script>
</c:if>
</body>
</html>
