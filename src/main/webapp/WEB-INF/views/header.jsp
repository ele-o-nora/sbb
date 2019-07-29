<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Westerosi Railways</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/webjars/bootstrap/4.3.1/css/bootstrap.min.css" />
</head>
<body>
    <nav class="navbar navbar-dark bg-dark">
        <a href="${pageContext.request.contextPath}/"
           class="navbar-brand text-danger text-uppercase font-weight-bold">Westerosi Railways</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse"
                data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/" class="nav-link">Home</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/admin" class="nav-link">Admin panel</a>
            </li>
        </ul>
        </div>
    </nav>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/webjars/jquery/3.4.1/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/webjars/momentjs/2.24.0/min/moment.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/webjars/bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>
</body>
</html>
