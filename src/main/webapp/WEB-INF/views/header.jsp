<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Westerosi Railways</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/webjars/bootstrap/4.3.1/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/webjars/tempusdominus-bootstrap-4/5.1.2/css/tempusdominus-bootstrap-4.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/font-awesome/5.9.0/css/fontawesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/font-awesome/5.9.0/css/regular.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/font-awesome/5.9.0/css/solid.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
</head>
<body>
<nav class="navbar navbar-dark navbar-expand-md bg-dark">
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
            <sec:authorize access="hasRole('ADMIN')">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/" class="nav-link">Admin panel</a>
                </li>
            </sec:authorize>
        </ul>
        <sec:authorize access="!isAuthenticated()">
            <button class="btn btn-sm btn-outline-light float-right m-1" type="button" id="signInButton">Sign in
            </button>
            <button class="btn btn-sm btn-outline-light float-right m-1" type="button" id="signUpButton">Sign up
            </button>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <form action="${pageContext.request.contextPath}/logout" method="POST" class="form-inline">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="submit" value="Logout" class="btn btn-sm btn-outline-light float-right m-1"/>
            </form>
        </sec:authorize>
    </div>
</nav>
<div class="modal fade" id="signUpModal" tabindex="-1" role="dialog" aria-labelledby="signUpTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="signUpTitle">Sign up</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/register" method="post">
                    <div class="form-row justify-content-center m-3">
                        <div class="col-sm-6">
                            <input type="text" name="firstName" placeholder="First name" class="form-control" required>
                        </div>
                        <div class="col-sm-6">
                            <input type="text" name="lastName" placeholder="Last name" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-row justify-content-center m-3">
                        <div class="col-sm-6">
                            <input type="text" name="dateOfBirth" placeholder="Date of birth"
                                   class="form-control datetimepicker-input" id="dateOfBirthPicker"
                                   data-toggle="datetimepicker" data-target="#dateOfBirthPicker" required>
                        </div>
                        <div class="col-sm-6">
                            <input type="email" name="email" placeholder="E-mail" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-row justify-content-center m-3">
                        <div class="col-sm-6">
                            <input type="password" name="password" placeholder="Password" class="form-control" id="password"
                                   pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}" title="Password should be at least 6 symbols long,
with at least one number, one lowercase and one uppercase letter" onkeyup="
  this.setCustomValidity(this.validity.patternMismatch ? this.title : '');
  form.matchingPassword.pattern = RegExp.escape(this.value);
" required>
                        </div>
                        <div class="col-sm-6">
                            <input type="password" name="matchingPassword" placeholder="Confirm password" id="confirmPassword"
                                   class="form-control" title="Password should match the first one" required onkeyup="
  this.setCustomValidity(this.validity.patternMismatch ? this.title : '');
">
                        </div>
                    </div>
                    <div class="form-row justify-content-center m-3">
                        <input type="submit" value="Sign up" class="btn btn-outline-secondary">
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="signInModal" tabindex="-1" role="dialog" aria-labelledby="signInTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="signInTitle">Sign in</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/perform_login" method="POST">
                    <div class="form-row justify-content-center">
                        <div class="col-sm-5">
                            <input type="text" class="form-control" name="username"
                                   placeholder="Username"/>
                        </div>
                        <div class="col-sm-5">
                            <input type="password" class="form-control" name="password"
                                   placeholder="Password"/>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="submit" value="Submit"
                           class="btn btn-outline-secondary m-3"/>
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    // polyfill for RegExp.escape
    if(!RegExp.escape) {
        RegExp.escape = function(s) {
            return String(s).replace(/[\\^$*+?.()|[\]{}]/g, '\\$&');
        };
    }
</script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/webjars/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/webjars/momentjs/2.24.0/min/moment.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/webjars/bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/webjars/tempusdominus-bootstrap-4/5.1.2/js/tempusdominus-bootstrap-4.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</body>
</html>
