<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Railways</title>
</head>
<body>
<h2>Current schedule</h2>
<form action="/curSchedule" method="post">
    <label>Station: </label>
    <input type="text" name="stationName"/>
    <input type="submit" />
</form>
</body>
</html>
