<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add new station</title>
</head>
<body class="text-center">
<%@include file="header.jsp" %>
<h4 class="text-secondary m-4">Add new station to <span class="text-danger">${line.name}</span> line</h4>
<div class="row">
    <div class="col-sm-6 offset-3">
        <c:forEach var="station" items="${stations}" varStatus="status">
            <c:if test="${status.first}">
                <input type="button" value="+" onclick="showAddStationForm(1)"
                       class="btn btn-outline-dark rounded-circle m-2"><br/></c:if>
            <span class="text-secondary">${station.name}</span><br/>
            <input type="button" value="+" onclick="showAddStationForm(${status.index+2})"
                   class="btn btn-outline-dark rounded-circle m-2"><br/>
        </c:forEach>
    </div>
</div>

<div class="modal fade" id="addStationModal" tabindex="-1" role="dialog" aria-labelledby="stationAddTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="stationAddTitle">Add new station</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/admin/addNewStation"
                      method="post" id="addStationForm" class="form-horizontal">
                    <input type="hidden" name="order" id="order" value="0">
                    <input type="hidden" name="line" value="${line.id}">
                    <div class="form-row justify-content-center m-2">
                        <div class="col-sm-9">
                            <input type="text" placeholder="Station name" name="name"
                                   id="addStationName" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-row justify-content-center m-2">
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="x"
                                   placeholder="x coordinate" pattern="[1-9]{1}[0-9]{0,3}"
                                   title="Please enter valid coordinate"
                                   onkeyup="this.setCustomValidity(this.validity.patternMismatch ? this.title : '');">
                        </div>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="y"
                                   placeholder="y coordinate" pattern="[1-9]{1}[0-9]{0,3}"
                                   title="Please enter valid coordinate"
                                   onkeyup="this.setCustomValidity(this.validity.patternMismatch ? this.title : '');">
                        </div>
                    </div>
                    <div class="form-row justify-content-center m-2">
                        <input type="submit" value="Add station" class="btn btn-outline-secondary">
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
