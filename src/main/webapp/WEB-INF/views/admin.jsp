<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin panel</title>
</head>
<body>
<%@include file="header.jsp" %>
<div id="lines">Add new station</div>
    <ul id="linesList" style="display:none">
        <c:forEach var="line" items="${lines}">
            <li><a href="${pageContext.request.contextPath}/admin/addStation/${line.id}">${line.name}</a></li>
        </c:forEach>
    </ul>

    <script type="text/javascript">
        $('#lines').click(function() {
            $('#linesList').toggle();
        });
    </script>
</body>
</html>
