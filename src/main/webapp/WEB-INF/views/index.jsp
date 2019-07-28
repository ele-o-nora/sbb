<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Westerosi Railways</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/webjars/tempusdominus-bootstrap-4/5.1.2/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/font-awesome/5.9.0/css/fontawesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/font-awesome/5.9.0/css/regular.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/font-awesome/5.9.0/css/solid.min.css">
</head>
<body>
<%@include file="header.jsp" %>
<h2>Search for connection:</h2>
<form action="${pageContext.request.contextPath}/findTrains" method="post">
    <div class="container">
        <div class="form-row m-1">
            <div class="col-sm-3 offset-3">
                <input class="form-control" list="stations" autocomplete="off"
                       placeholder="From" name="stationFrom"/>
            </div>
            <div class="col-sm-3">
                <input class="form-control" list="stations" autocomplete="off"
                       placeholder="To" name="stationTo"/>
            </div>
        </div>
        <div class="form-row m-1">
            <div class='col-sm-4 offset-3'>
                <input type="text" class="form-control datetimepicker-input" name="dateTime"
                       id="datetimepicker" data-toggle="datetimepicker" data-target="#datetimepicker" />
            </div>
            <script type="text/javascript">
                $(function () {
                    $('#datetimepicker').datetimepicker({
                        icons: {
                            time: 'far fa-clock',
                            date: 'far fa-calendar',
                            up: 'fas fa-arrow-up',
                            down: 'fas fa-arrow-down',
                            previous: 'fas fa-chevron-left',
                            next: 'fas fa-chevron-right',
                            today: 'far fa-calendar-check-o',
                            clear: 'far fa-trash',
                            close: 'far fa-times'
                        },
                        minDate: moment(),
                        maxDate: moment().add(2, 'months'),
                        format: 'YYYY-MM-DD HH:mm'
                    });
                });
            </script>
            <div class="col-sm-1">
                <div class="btn-group btn-group-toggle" data-toggle="buttons">
                    <label class="btn btn-secondary active">
                        <input type="radio" name="searchType" value="departure"
                               autocomplete="off" checked> From
                    </label>
                    <label class="btn btn-secondary">
                        <input type="radio" name="searchType" value="arrival"
                               autocomplete="off"> To
                    </label>
                </div>
            </div>
            <div class="col-sm-1">
                <input type="submit" value="Search" class="btn btn-outline-secondary">
            </div>
        </div>
    </div>
</form>
<h2>Or look up current schedule:</h2>
<form action="${pageContext.request.contextPath}/curSchedule" method="post">
    <input list="stations" name="stationName" placeholder="Station" autocomplete="off"/>
    <input type="submit" value="Schedule"/>
</form>
<datalist id="stations">
    <c:forEach var="station" items="${stations}">
        <option>${station.name}</option>
    </c:forEach>
</datalist>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/momentjs/2.24.0/min/moment.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/webjars/tempusdominus-bootstrap-4/5.1.2/js/tempusdominus-bootstrap-4.min.js"></script>
</body>
</html>
