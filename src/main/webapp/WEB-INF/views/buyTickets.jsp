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
    <p class="text-danger m-5">${status}</p>
    <a href="${pageContext.request.contextPath}/" class="text-secondary">Back to main page</a>
</c:if>
<c:if test="${empty status}"><h5 class="text-secondary m-3">Passenger details:</h5>
<c:if test="${!empty ticketOrder}">
    <form:form modelAttribute="ticketOrder" method="post"
               action="${pageContext.request.contextPath}/finalizeTicketSale">
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-3">
                <input type="text" class="form-control" name="firstName" placeholder="First name"
                       <c:if test="${!empty passenger}">value="${passenger.firstName}"</c:if>
                       autocomplete="off" required/>
            </div>
            <div class="col-sm-3">
                <input type="text" class="form-control" name="lastName" placeholder="Last name"
                       <c:if test="${!empty passenger}">value="${passenger.lastName}"</c:if>
                       autocomplete="off" required/>
            </div>
        </div>
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-3">
                <input type="text" name="dateOfBirth" placeholder="Date of birth"
                       class="form-control datetimepicker-input" id="dateOfBirthPicker2"
                       data-toggle="datetimepicker" data-target="#dateOfBirthPicker2"
                       autocomplete="off" required/>
            </div>
        </div>
        <h5 class="text-secondary m-3">Travel info:</h5>
        <div class="row justify-content-center m-2">
            <div class="col-sm-6 m-1 border p-1">
                    <div class="row mt-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Route:</span> ${ticketOrder.journey.route}
                    </div>
                    <div class="row mb-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Direction:</span> ${ticketOrder.journey.destination}
                    </div>
                <div class="row m-2 p-2 border-top border-bottom">
                    <div class="col-sm-6">
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">From: </span> ${ticketOrder.origin.station}
                        </div>
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">Departure: </span>${ticketOrder.origin.departure}
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">To: </span>${ticketOrder.destination.station}
                        </div>
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">Arrival: </span>${ticketOrder.destination.arrival}
                        </div>
                    </div>
                </div>
                <div class="row m-2 justify-content-center">
                    <span class="font-weight-bold mr-1">Price: </span>${ticketOrder.formattedPrice}
                </div>
                <form:input type="hidden" path="journey.id" value="${ticketOrder.journey.id}"/>
                <form:input type="hidden" path="origin.id" value="${ticketOrder.origin.id}"/>
                <form:input type="hidden" path="destination.id" value="${ticketOrder.destination.id}"/>
                <form:input type="hidden" path="price" value="${ticketOrder.price}"/>
            </div>
        </div>
        <h5 class="text-secondary m-3">Payment info:</h5>
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-3">
                <input type="text" class="form-control" placeholder="Card number"  id="cardNumber"
                       pattern="[0-9]{16,18}" title="16 or 18 digits" required>
            </div>
            <div class="col-sm-3">
                <input type="text" class="form-control" placeholder="Cardholder name" required>
            </div>
        </div>
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-2">
                <input type="text" class="form-control datetimepicker-input"
                       id="validUntilPicker" data-toggle="datetimepicker" data-target="#validUntilPicker"
                       placeholder="Valid until" required/>
            </div>
            <div class="col-sm-2">
                <input type="password" class="form-control" placeholder="CVV/CVC" id="cvc"
                       pattern="[0-9]{3}" title="3 digits" required>
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
            <div class="col-sm-3">
                <input type="text" class="form-control" name="firstName" placeholder="First name"
                       <c:if test="${!empty passenger}">value="${passenger.firstName}"</c:if>
                       autocomplete="off" required/>
            </div>
            <div class="col-sm-3">
                <input type="text" class="form-control" name="lastName" placeholder="Last name"
                       <c:if test="${!empty passenger}">value="${passenger.lastName}"</c:if>
                       autocomplete="off" required/>
            </div>
        </div>
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-3">
                <input type="text" name="dateOfBirth" placeholder="Date of birth"
                       class="form-control datetimepicker-input" id="dateOfBirthPicker2"
                       data-toggle="datetimepicker" data-target="#dateOfBirthPicker2"
                       autocomplete="off" required/>
            </div>
        </div>
        <h5 class="text-secondary m-3">Travel info:</h5>
        <div class="row justify-content-center m-2">
            <div class="col-sm-5 m-1 border p-1">
                    <div class="row mt-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Route:</span> ${transferTickets.firstTrain.journey.route}
                    </div>
                    <div class="row mb-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Direction:</span> ${transferTickets.firstTrain.journey.destination}
                    </div>
                <div class="row m-2 p-2 border-top border-bottom">
                    <div class="col-sm-6">
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">From: </span> ${transferTickets.firstTrain.origin.station}
                        </div>
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">Departure: </span>${transferTickets.firstTrain.origin.departure}
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">To: </span>${transferTickets.firstTrain.destination.station}
                        </div>
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">Arrival: </span>${transferTickets.firstTrain.destination.arrival}
                        </div>
                    </div>
                </div>
                <div class="row m-2 justify-content-center">
                    <span class="font-weight-bold mr-1">Price: </span>${transferTickets.firstTrain.formattedPrice}
                </div>
                <form:input type="hidden" path="firstTrain.journey.id"
                            value="${transferTickets.firstTrain.journey.id}"/>
                <form:input type="hidden" path="firstTrain.origin.id" value="${transferTickets.firstTrain.origin.id}"/>
                <form:input type="hidden" path="firstTrain.destination.id"
                            value="${transferTickets.firstTrain.destination.id}"/>
                <form:input type="hidden" path="firstTrain.price" value="${transferTickets.firstTrain.price}"/>
            </div>
            <div class="col-sm-5 m-1 border p-1">
                <div class="row mt-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Route:</span> ${transferTickets.secondTrain.journey.route}
                    </div>
                    <div class="row mb-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Direction:</span> ${transferTickets.secondTrain.journey.destination}
                    </div>
                <div class="row m-2 p-2 border-top border-bottom">
                    <div class="col-sm-6">
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">From: </span> ${transferTickets.secondTrain.origin.station}
                        </div>
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">Departure: </span>${transferTickets.secondTrain.origin.departure}
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">To: </span>${transferTickets.secondTrain.destination.station}
                        </div>
                        <div class="row justify-content-center">
                            <span class="font-weight-bold mr-1">Arrival: </span>${transferTickets.secondTrain.destination.arrival}
                        </div>
                    </div>
                </div>
                <div class="row m-2 justify-content-center">
                    <span class="font-weight-bold mr-1">Price: </span>${transferTickets.secondTrain.formattedPrice}
                </div>
                <form:input type="hidden" path="secondTrain.journey.id"
                            value="${transferTickets.secondTrain.journey.id}"/>
                <form:input type="hidden" path="secondTrain.origin.id"
                            value="${transferTickets.secondTrain.origin.id}"/>
                <form:input type="hidden" path="secondTrain.destination.id"
                            value="${transferTickets.secondTrain.destination.id}"/>
                <form:input type="hidden" path="secondTrain.price" value="${transferTickets.secondTrain.price}"/>
            </div>
        </div>
        <h5 class="text-secondary m-3">Payment info:</h5>
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-3">
                <input type="text" class="form-control" placeholder="Card number" id="cardNumber"
                       pattern="[0-9]{16,18}" title="16 or 18 digits" required>
            </div>
            <div class="col-sm-3">
                <input type="text" class="form-control" placeholder="Cardholder name" required>
            </div>
        </div>
        <div class="form-row justify-content-center m-1">
            <div class="col-sm-2">
                <input type="text" class="form-control datetimepicker-input"
                       id="validUntilPicker" data-toggle="datetimepicker" data-target="#validUntilPicker"
                       placeholder="Valid until" required/>
            </div>
            <div class="col-sm-2">
                <input type="password" class="form-control" placeholder="CVV/CVC"  id="cvc"
                       title="3 digits" pattern="[0-9]{3}" required>
            </div>
        </div>
        <div class="form-row justify-content-center m-2">
            <input type="submit" value="Buy tickets" class="btn btn-outline-danger">
        </div>
    </form:form>
</c:if>
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
