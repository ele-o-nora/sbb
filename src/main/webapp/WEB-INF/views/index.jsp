<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Westerosi Railways</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<div class="container m-5">
    <h6 class="text-muted">Search for connection:</h6>
    <form action="${pageContext.request.contextPath}/findTrains" method="post">
        <div class="form-row m-1">
            <div class="col-sm-4 offset-2">
                <input class="form-control" list="stations" autocomplete="off"
                       placeholder="From" name="stationFrom" required/>
            </div>
            <div class="col-sm-4">
                <input class="form-control" list="stations" autocomplete="off"
                       placeholder="To" name="stationTo" required/>
            </div>
        </div>
        <div class="form-row m-1">
            <div class='col-sm-4 offset-2'>
                <input type="text" class="form-control datetimepicker-input" name="dateTime"
                       id="datetimepicker" data-toggle="datetimepicker" data-target="#datetimepicker" required/>
            </div>
            <div class="col-sm-2 my-auto">
                <div class="btn-group btn-group-toggle" data-toggle="buttons">
                    <label class="btn btn-secondary btn-sm active">
                        <input type="radio" name="searchType" value="departure"
                               autocomplete="off" checked> Departure
                    </label>
                    <label class="btn btn-secondary btn-sm">
                        <input type="radio" name="searchType" value="arrival"
                               autocomplete="off"> Arrival
                    </label>
                </div>
            </div>
            <div class="col-sm-2">
                <input type="submit" value="Find trains" class="btn btn-outline-secondary float-right">
            </div>
        </div>
    </form>
</div>
<div class="container m-5">
    <h6 class="text-muted">Or look up current schedule:</h6>
    <form action="${pageContext.request.contextPath}/curSchedule" method="post">
        <div class="form-row">
            <div class="col-sm-5 offset-sm-3">
                <input list="stations" class="form-control" name="stationName" placeholder="Station" autocomplete="off"
                       required/>
            </div>
            <div class="col-sm-1">
                <input type="submit" value="Go" class="btn btn-outline-secondary"/>
            </div>
        </div>
    </form>
</div>
<datalist id="stations">
    <c:forEach var="station" items="${stations}">
        <option>${station.name}</option>
    </c:forEach>
</datalist>
</body>
</html>
