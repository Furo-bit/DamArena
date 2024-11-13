<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Play With Friends</title>
    <link rel="icon" type="image/x-icon" href="/Images/logo_damarena.jpg">
    <link rel="stylesheet" type="text/css" href="/css/create_account_style.css">
    <link rel="stylesheet" type="text/css" href="/css/game_mode_style.css">
</head>
<body>
<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>

<div class="container-game-mode">
    <h1>Challenge a friend</h1>
    <table>
        <thead>
        <tr>
            <th>Nickname</th>
            <th style="text-align: center;">Points</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="friend" items="${friends}">
            <tr>
                <td style="cursor: pointer;" onclick="window.location='/play-friend-options?friendId=${friend.email}'">${friend.username}</td>
                <td style="text-align: center;">500</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
