<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Play Online</title>
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
    <h1>Play Online</h1>

    <h2>Choose a time format</h2>
    <div>
        <label for="time1" class="custom-radio" onclick="selectRadio('time1')">
            <input type="radio" id="time1" name="timeControl" value="1" style="display: none;">
            1
        </label>
        <label for="time2" class="custom-radio" onclick="selectRadio('time2')">
            <input type="radio" id="time2" name="timeControl" value="1+1" style="display: none;">
            1+1
        </label>
        <label for="time3" class="custom-radio" onclick="selectRadio('time3')">
            <input type="radio" id="time3" name="timeControl" value="2+1" style="display: none;">
            2+1
        </label>
        <label for="time4" class="custom-radio" onclick="selectRadio('time4')">
            <input type="radio" id="time4" name="timeControl" value="3" style="display: none;">
            3
        </label>
        <label for="time5" class="custom-radio" onclick="selectRadio('time5')">
            <input type="radio" id="time5" name="timeControl" value="3+2" style="display: none;">
            3+2
        </label>
        <label for="time6" class="custom-radio" onclick="selectRadio('time6')">
            <input type="radio" id="time6" name="timeControl" value="5" style="display: none;">
            5
        </label>
        <label for="time7" class="custom-radio" onclick="selectRadio('time7')">
            <input type="radio" id="time7" name="timeControl" value="10" style="display: none;">
            10
        </label>
        <label for="time8" class="custom-radio" onclick="selectRadio('time8')">
            <input type="radio" id="time8" name="timeControl" value="15+10" style="display: none;">
            15+10
        </label>
        <label for="time9" class="custom-radio" onclick="selectRadio('time9')">
            <input type="radio" id="time9" name="timeControl" value="30" style="display: none;">
            30
        </label>
    </div>

    <br>

    <button type="button" onclick="startGame()">Start Game</button>

    <div class="loading-screen" id="loadingScreen">
        <div class="loading-spinner">Loading...</div>
    </div>
</div>
<script>
    function selectRadio(id) {
        var radios = document.getElementsByName(document.getElementById(id).name);
        radios.forEach(function (radio) {
            radio.parentNode.classList.remove('selected');
        });
        document.getElementById(id).parentNode.classList.add('selected');
    }

    function startGame() {
        document.getElementById('loadingScreen').style.display = 'block';
        setTimeout(function () {

            document.getElementById('loadingScreen').style.display = 'none';

            alert("Game started!");
        }, 2000);
    }
</script>
</body>
</html>
