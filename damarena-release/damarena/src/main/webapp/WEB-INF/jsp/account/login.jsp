<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>

    <link rel="stylesheet" type="text/css" href="/css/index.css">
    <link rel="stylesheet" type="text/css" href="/css/login_style.css">
</head>
<body>

<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>

<div class="container">
    <h2>Login</h2>

    <form id="loginForm" action="/authenticate" method="post">
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="userEmail" required="">
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="userPassword" required="">
        </div>
        <div class="form-group">
            <input type="submit" id="loginBtn" value="Login" disabled="">
        </div>
    </form>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        var emailInput = document.getElementById("email");
        var passwordInput = document.getElementById("password");
        var loginBtn = document.getElementById("loginBtn");

        emailInput.addEventListener("input", toggleLoginButton);
        passwordInput.addEventListener("input", toggleLoginButton);

        function toggleLoginButton() {
            if (emailInput.value.trim() !== "" && passwordInput.value.trim() !== "") {
                loginBtn.removeAttribute("disabled");
            } else {
                loginBtn.setAttribute("disabled", "disabled");
            }
        }
    });
</script>
</body>
</html>
