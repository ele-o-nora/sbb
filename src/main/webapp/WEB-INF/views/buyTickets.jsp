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
        <form:form modelAttribute="buyerDetails" method="post"
                   action="${pageContext.request.contextPath}/finalizeTicketSale">
            <form:errors path = "*" cssClass = "text-danger" element = "div" />
            <div class="form-row justify-content-center m-1">
                <c:if test="${empty passenger}">
                    <div class="col-sm-3">
                        <form:input type="text" class="form-control" path="passenger.firstName" placeholder="First name"
                                    autocomplete="off" required="required"/>
                    </div>
                    <div class="col-sm-3">
                        <form:input type="text" class="form-control" path="passenger.lastName" placeholder="Last name"
                                    autocomplete="off" required="required"/>
                    </div>
                </c:if>
                <c:if test="${!empty passenger}">
                    <div class="col-sm-3">
                        <form:input type="text" class="form-control" path="passenger.firstName" placeholder="First name"
                                    value="${passenger.firstName}" autocomplete="off" required="required"/>
                    </div>
                    <div class="col-sm-3">
                        <form:input type="text" class="form-control" path="passenger.lastName" placeholder="Last name"
                                    value="${passenger.lastName}" autocomplete="off" required="required"/>
                    </div>
                </c:if>
            </div>
            <div class="form-row justify-content-center m-1">
                <div class="col-sm-3">
                    <form:input type="text" path="passenger.dateOfBirth" placeholder="Date of birth"
                                class="form-control datetimepicker-input" id="dateOfBirthPicker2"
                                data-toggle="datetimepicker" data-target="#dateOfBirthPicker2"
                                autocomplete="off" required="required"/>
                </div>
            </div>
            <h5 class="text-secondary m-3">Travel info:</h5>
            <div class="row justify-content-center m-2">
                <div class="col-sm-6 m-1 border p-1">
                    <div class="row mt-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Route:</span> ${sessionScope.ticketOrder.journey.route}
                    </div>
                    <div class="row mb-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Direction:</span> ${sessionScope.ticketOrder.journey.destination}
                    </div>
                    <div class="row m-2 p-2 border-top border-bottom">
                        <div class="col-sm-6">
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">From: </span> ${sessionScope.ticketOrder.origin.station}
                            </div>
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">Departure: </span>${sessionScope.ticketOrder.origin.departure}
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">To: </span>${sessionScope.ticketOrder.destination.station}
                            </div>
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">Arrival: </span>${sessionScope.ticketOrder.destination.arrival}
                            </div>
                        </div>
                    </div>
                    <div class="row m-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Price: </span>${sessionScope.ticketOrder.formattedPrice}
                    </div>
                </div>
            </div>
            <h5 class="text-secondary m-3">Payment info:</h5>
            <div class="form-row justify-content-center m-1">
                <div class="col-sm-3">
                    <form:input type="text" class="form-control" placeholder="Card number" id="cardNumber"
                                path="payment.cardNumber" pattern="[0-9]{16,18}" title="16 or 18 digits"
                                required="required"/>
                </div>
                <div class="col-sm-3">
                    <form:input type="text" class="form-control" placeholder="Cardholder name"
                                path="payment.cardHolderName" required="required"/>
                </div>
            </div>
            <div class="form-row justify-content-center m-1">
                <div class="col-sm-2">
                    <form:input type="text" class="form-control datetimepicker-input"
                                id="validUntilPicker" data-toggle="datetimepicker" data-target="#validUntilPicker"
                                placeholder="Valid until" path="payment.validUntil" required="required"/>
                </div>
                <div class="col-sm-2">
                    <form:input type="password" class="form-control" placeholder="CVV/CVC" id="cvc"
                                pattern="[0-9]{3}" title="3 digits" required="required" path="payment.cvc"/>
                </div>
            </div>
            <div class="form-row justify-content-center m-2">
                <input type="submit" class="btn btn-outline-danger" value="Buy ticket"/>
            </div>
        </form:form>
    </c:if>
    <c:if test="${!empty transferTickets}">
        <form:form modelAttribute="buyerDetails" method="post"
                   action="${pageContext.request.contextPath}/finalizeTicketsSale" autocomplete="off">
            <form:errors path = "*" cssClass = "text-danger" element = "div" />
            <div class="form-row justify-content-center m-1">
                <c:if test="${empty passenger}">
                    <div class="col-sm-3">
                        <form:input type="text" class="form-control" path="passenger.firstName" placeholder="First name"
                                    autocomplete="off" required="required"/>
                    </div>
                    <div class="col-sm-3">
                        <form:input type="text" class="form-control" path="passenger.lastName" placeholder="Last name"
                                    autocomplete="off" required="required"/>
                    </div>
                </c:if>
                <c:if test="${!empty passenger}">
                    <div class="col-sm-3">
                        <form:input type="text" class="form-control" path="passenger.firstName" placeholder="First name"
                                    value="${passenger.firstName}" autocomplete="off" required="required"/>
                    </div>
                    <div class="col-sm-3">
                        <form:input type="text" class="form-control" path="passenger.lastName" placeholder="Last name"
                                    value="${passenger.lastName}" autocomplete="off" required="required"/>
                    </div>
                </c:if>
            </div>
            <div class="form-row justify-content-center m-1">
                <div class="col-sm-3">
                    <form:input type="text" path="passenger.dateOfBirth" placeholder="Date of birth"
                                class="form-control datetimepicker-input" id="dateOfBirthPicker2"
                                data-toggle="datetimepicker" data-target="#dateOfBirthPicker2"
                                autocomplete="off" required="required"/>
                </div>
            </div>
            <h5 class="text-secondary m-3">Travel info:</h5>
            <div class="row justify-content-center m-2">
                <div class="col-sm-5 m-1 border p-1">
                    <div class="row mt-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Route:</span> ${sessionScope.transferTickets.firstTrain.journey.route}
                    </div>
                    <div class="row mb-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Direction:</span> ${sessionScope.transferTickets.firstTrain.journey.destination}
                    </div>
                    <div class="row m-2 p-2 border-top border-bottom">
                        <div class="col-sm-6">
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">From: </span> ${sessionScope.transferTickets.firstTrain.origin.station}
                            </div>
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">Departure: </span>${sessionScope.transferTickets.firstTrain.origin.departure}
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">To: </span>${sessionScope.transferTickets.firstTrain.destination.station}
                            </div>
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">Arrival: </span>${sessionScope.transferTickets.firstTrain.destination.arrival}
                            </div>
                        </div>
                    </div>
                    <div class="row m-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Price: </span>${sessionScope.transferTickets.firstTrain.formattedPrice}
                    </div>
                </div>
                <div class="col-sm-5 m-1 border p-1">
                    <div class="row mt-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Route:</span> ${sessionScope.transferTickets.secondTrain.journey.route}
                    </div>
                    <div class="row mb-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Direction:</span> ${sessionScope.transferTickets.secondTrain.journey.destination}
                    </div>
                    <div class="row m-2 p-2 border-top border-bottom">
                        <div class="col-sm-6">
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">From: </span> ${sessionScope.transferTickets.secondTrain.origin.station}
                            </div>
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">Departure: </span>${sessionScope.transferTickets.secondTrain.origin.departure}
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">To: </span>${sessionScope.transferTickets.secondTrain.destination.station}
                            </div>
                            <div class="row justify-content-center">
                                <span class="font-weight-bold mr-1">Arrival: </span>${sessionScope.transferTickets.secondTrain.destination.arrival}
                            </div>
                        </div>
                    </div>
                    <div class="row m-2 justify-content-center">
                        <span class="font-weight-bold mr-1">Price: </span>${sessionScope.transferTickets.secondTrain.formattedPrice}
                    </div>
                </div>
            </div>
            <h5 class="text-secondary m-3">Payment info:</h5>
            <div class="form-row justify-content-center m-1">
                <div class="col-sm-3">
                    <form:input type="text" class="form-control" placeholder="Card number" id="cardNumber"
                                pattern="[0-9]{16,18}" title="16 or 18 digits"
                                path="payment.cardNumber" required="required"/>
                </div>
                <div class="col-sm-3">
                    <form:input type="text" class="form-control" placeholder="Cardholder name"
                                path="payment.cardHolderName" required="required"/>
                </div>
            </div>
            <div class="form-row justify-content-center m-1">
                <div class="col-sm-2">
                    <form:input type="text" class="form-control datetimepicker-input"
                                id="validUntilPicker" data-toggle="datetimepicker" data-target="#validUntilPicker"
                                placeholder="Valid until" path="payment.validUntil" required="required"/>
                </div>
                <div class="col-sm-2">
                    <form:input type="password" class="form-control" placeholder="CVV/CVC" id="cvc"
                                title="3 digits" pattern="[0-9]{3}" path="payment.cvc" required="required"/>
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
