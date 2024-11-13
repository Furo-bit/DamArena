<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Game Selection</title>
    <link rel="icon" type="image/x-icon" href="/Images/logo_damarena.jpg">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap">
    <link rel="stylesheet" type="text/css" href="/css/create_account_style.css">
    <link rel="stylesheet" type="text/css" href="/css/game_mode_style.css">
</head>
<style>

</style>
<body>
<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>
<div class="container-game-mode">
    <h1>Select Game Mode:</h1>
    <c:if test="${isLoggedin}">
        <a href="/single-player">
            <button>Single Player</button>
        </a>
        <a href="/play-friends">
            <button>Play with Friends</button>
        </a>
        <a href="/play-online">
            <button>Play Online</button>
        </a>
    </c:if>
    <c:if test="${not isLoggedin}">
        <a href="/single-player">
            <button>Single Player</button>
        </a>
        <button style="background-color: #ddd" disabled>Play with Friends</button>
        </a>
        <button style="background-color: #ddd" disabled>Play Online</button>
        </a>
    </c:if>
</div>
</body>
</html>
