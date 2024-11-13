<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign up</title>
    <link rel="stylesheet" type="text/css" href="/css/index.css">
    <link rel="stylesheet" type="text/css" href="/css/create_account_style.css">
    <script>
        function validateForm() {
            var username = document.getElementById("username").value;
            var email = document.getElementById("email").value;
            var password = document.getElementById("password").value;
            var confirmPassword = document.getElementById("confirmPassword").value;
            var name = document.getElementById("name").value;
            var surname = document.getElementById("surname").value;
            var submitButton = document.getElementById("submitButton");

            // Reset error messages
            document.getElementById("usernameError").innerHTML = "";
            document.getElementById("emailError").innerHTML = "";
            document.getElementById("passwordError").innerHTML = "";
            document.getElementById("confirmPasswordError").innerHTML = "";
            document.getElementById("nameError").innerHTML = "";
            document.getElementById("surnameError").innerHTML = "";

            // Check if any field is empty
            if (username.trim() === "") {
                document.getElementById("usernameError").innerHTML = "Username is required.";
            }
            if (email.trim() === "") {
                document.getElementById("emailError").innerHTML = "Email is required.";
            }
            if (password.trim() === "") {
                document.getElementById("passwordError").innerHTML = "Password is required.";
            }
            if (confirmPassword.trim() === "") {
                document.getElementById("confirmPasswordError").innerHTML = "Please confirm your password.";
            }
            if (name.trim() === "") {
                document.getElementById("nameError").innerHTML = "Name is required.";
            }
            if (surname.trim() === "") {
                document.getElementById("surnameError").innerHTML = "Surname is required.";
            }

            submitButton.disabled = (
                username.trim() === "" ||
                email.trim() === "" ||
                password.trim() === "" ||
                confirmPassword.trim() === "" ||
                name.trim() === "" ||
                surname.trim() === ""
            );

            var passwordPattern = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[A-Z]).{6,}$/;
            if (password.trim() !== "") {
                if (!passwordPattern.test(password)) {
                    document.getElementById("passwordError").innerHTML = "Password must be at least 6 characters long and contain a number, a special character and an uppercase letter.";
                    submitButton.disabled = true;
                }
            }

            if (confirmPassword.trim() !== "") {
                if (password !== confirmPassword) {
                    document.getElementById("confirmPasswordError").innerHTML = "Passwords do not match.";
                    submitButton.disabled = true;
                }
            }
        }
    </script>
</head>
<body>
<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>
<div class="container">
    <h2>Sign up</h2>
    <form action="/add-user" method="post" oninput="validateForm()">
        <div class="form-group">
            <label for="name">Name:</label>
            <input style="width: 380px" type="text" id="name" name="name" required>
            <span id="nameError" style="color: red;"></span>
        </div>
        <div class="form-group">
            <label for="surname">Surname:</label>
            <input style="width: 380px" type="text" id="surname" name="surname" required>
            <span id="surnameError" style="color: red;"></span>
        </div>
        <div class="form-group">
            <label for="username">Username:</label>
            <input style="width: 380px" type="text" id="username" name="username" required>
            <span id="usernameError" style="color: red;"></span>
        </div>
        <div class="form-group">
            <label for="email">Email:</label>
            <input style="width: 380px" type="email" id="email" name="email" required>
            <span id="emailError" style="color: red;"></span>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input style="width: 380px" type="password" id="password" name="password" required>
            <span id="passwordError" style="color: red;"></span>
        </div>
        <div class="form-group">
            <label for="confirmPassword">Confirm Password:</label>
            <input style="width: 380px" type="password" id="confirmPassword" name="confirmPassword" required>
            <span id="confirmPasswordError" style="color: red;"></span>
        </div>
        <input type="submit" id="submitButton" value="Register" disabled>
    </form>
</div>
</body>
</html>
