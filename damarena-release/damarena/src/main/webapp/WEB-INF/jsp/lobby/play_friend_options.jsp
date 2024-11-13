<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Game Options</title>
    <link rel="icon" type="image/x-icon" href="/Images/logo_damarena.jpg">
    <link rel="stylesheet" type="text/css" href="/css/loading_screen.css">
    <link rel="stylesheet" type="text/css" href="/css/game_mode_style.css">
    <link rel="stylesheet" type="text/css" href="/css/create_account_style.css">
</head>
<body>
<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>

<div class="container-game-mode">
    <h1>Game Options</h1>

    <form action="/send-invite" method="post">
        <div class="form-group-game-options">
            <label for="minutes" class="game-options-label">Minutes:</label>
            <input type="number" id="minutes" name="minutes" min="0" max="60" value="10">
        </div>

        <div class="form-group-game-options">
            <label for="seconds" class="game-options-label">Seconds:</label>
            <input type="number" id="seconds" name="seconds" min="0" max="59" value="0">
        </div>

        <div class="form-group-game-options">
            <label for="color" class="game-options-label">Select Color:</label>
            <select id="color" name="color" class="custom-select">
                <option value="white">Play as White</option>
                <option value="black">Play as Black</option>
            </select>
        </div>


        <br><br>
        <div class="start-game-button">
            <input type="hidden" id="friendId" name="friendId" value="${friendId}">
            <button type="submit">Send Invite</button>
        </div>

    </form>
</div>
</body>
</html>
