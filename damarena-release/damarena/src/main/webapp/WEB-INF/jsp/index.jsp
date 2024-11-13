<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/x-icon" href="/Images/logo_damarena.jpg">
    <link rel="stylesheet" type="text/css" href="/css/index.css">
    <title>DamArena - Play Now!</title>
</head>
<body>
<div class="container">
    <div class="return-link">
        <a href="/">
            <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
        </a>
    </div>
    <header>
        <h1>Welcome to <span>DamArena</span>!</h1>
    </header>
    <c:if test="${isLoggedIn}">
        <div class="right-header">
            <img id="bellIcon" src="/Images/bell.svg" alt="Menu" width="50" height="50">
            <div class="notification-menu" id="notificationMenu">
                <c:if test="${empty notifications}">
                    No notification
                </c:if>
                <c:forEach var="notification" items="${notifications}">
                    <form action="/delete-notification" method="post">
                        <input type="hidden" name="notificationId" value="${notification.id}"/>
                        <button type="submit">${notification.message}</button>
                    </form>
                </c:forEach>
            </div>
        </div>
    </c:if>
    <nav class="menu">
        <ul>
            <li><a href="/rankings">Rankings</a></li>
            <li><a href="/tournaments">Tournaments</a></li>
            <li><a style="color: #cacaca;">Daily Challenge</a></li>

            <c:if test="${isLoggedIn}">
                <li><a href="/profile">Profile</a></li>
                <li><a href="/logout">Log out</a></li>
            </c:if>

            <c:if test="${not isLoggedIn}">
                <li><a href="/login">Login</a></li>
                <li><a href="/create-account">Sign up</a></li>
            </c:if>
        </ul>
    </nav>
    <div class="search-container">
        <center>
            <form action="/search" method="get">
                <input class="search-input" type="text" name="username" placeholder="Search by username">
                <button class="search-button" type="submit">Search</button>
            </form>
        </center>
    </div>
    <div class="play-checkers">

        <div class="play-checkers-btn" onclick="window.location='/game-mode'">
            <img src="/Images/playcheckers.svg" alt="Chessboard">
            <span>Checkers</span>
        </div>

        <div class="play-checkers-btn" style="background:#cacaca; cursor: default;">
            <img src="/Images/question-mark.svg" alt="Coming Soon">
            <span>Coming Soon</span>
        </div>
    </div>

</div>
<footer>
    <p>&copy; 2024 DamArena. All rights reserved.</p>
</footer>
<script>
    document.getElementById('bellIcon').addEventListener('click', function () {
        var notificationMenu = document.getElementById('notificationMenu');

        if (notificationMenu.style.display === 'none' || notificationMenu.style.display === '') {
            notificationMenu.style.display = 'block';
        } else {
            notificationMenu.style.display = 'none';
        }
    });
</script>
</body>
</html>
