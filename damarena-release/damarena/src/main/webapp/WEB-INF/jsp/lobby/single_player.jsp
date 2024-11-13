<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Single Player</title>
    <link rel="icon" type="image/x-icon" href="/Images/logo_damarena.jpg">
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
    <h2>Single Player Mode</h2>
    <form action="/single_player_game" method="post" class="form-container">
        <div class="form-group-game-options">
            <label class="game-options-label" for="difficulty">Select Difficulty:</label>
            <select id="difficulty" name="difficulty" class="custom-select">
                <option value="easy">Easy</option>
                <option value="medium">Medium</option>
                <option value="hard">Hard</option>
            </select>
        </div>

        <div class="form-group-game-options">
            <label class="game-options-label" for="color">Select Color:</label>
            <select id="color" name="color" class="custom-select">
                <option value="white">Play as White</option>
                <option value="black">Play as Black</option>
                <option value="random">Random Color</option>
            </select>
        </div>

        <br><br>

        <div class="start-game-button">
            <button type="submit">Start Game</button>
        </div>
    </form>

</body>
</html>

