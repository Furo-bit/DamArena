<%@ page import="com.project.damarena.model.CheckersGame" %>
<%@ page import="com.project.damarena.model.Piece" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="playerId" value="${sessionScope.userEmail}"/>
<!DOCTYPE html>
<html>
<head>
    <title>Checkers Game</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="/css/board_style.css">
</head>
<body onload="startUp()">
<div class="overlay"></div>
<div id="popup" class="popup">
    <div id="winner-text" class="winner-text"></div>
    <button onclick="returnHome()">Return Home</button>
</div>
<div id="game-board" class="board-container">
    <%
        CheckersGame game = (CheckersGame) request.getAttribute("game");
        Piece[][] boardArray;
        String loggedUserEmail = (String) session.getAttribute("userEmail");
        String player1Email = game.getPlayer1().getUser().getEmail();
        char opponentColor = !loggedUserEmail.equals(player1Email) ? 'w' : 'b';
        String currentPlayerEmail = game.getCurrentPlayer().getUser().getEmail();
    %>
    <div class="user-box  <%= !currentPlayerEmail.equals(loggedUserEmail) ? "current-player" : "inactive-player" %> ">
        <div class="color-bar <%= opponentColor == 'w' ? "white" : "black" %>"></div>
        <h3>${opponent}</h3>
    </div>
    <div class="board">
        <%
            if (loggedUserEmail.equals(player1Email)) {
                boardArray = game.getPlayer1().getBoard().getPieces();
            } else {
                boardArray = game.getPlayer2().getBoard().getPieces();
            }
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Piece piece = boardArray[row][col];
                    String pieceClass = "";
                    if (piece != null) {
                        if (piece.getPlayerColor() == 'w') {
                            pieceClass += "white";
                            if (piece.isKing()) {
                                pieceClass += " player_color_king";
                            }

                            if (game.getCurrentPlayer().getColor() == 'w') {
                                pieceClass += " current_player";
                            }

                        }

                        if (piece.getPlayerColor() == 'b') {
                            pieceClass += "black";
                            if (piece.isKing()) {
                                pieceClass += " opponent_color_king";
                            }

                            if (game.getCurrentPlayer().getColor() == 'b') {
                                pieceClass += " current_player";
                            }
                        }
                    }
        %>
        <div class="square <%= (row + col) % 2 == 0 ? "even" : "odd" %>" data-row="<%= row %>" data-col="<%= col %>">
            <% if (piece.getPlayerColor() != 'e') { %>
            <div class="piece <%= pieceClass %>"></div>
            <% } %>
        </div>
        <%
                }
            }
        %>
    </div>
    <div class="user-box <%= currentPlayerEmail.equals(loggedUserEmail) ? "current-player" : "inactive-player" %>">
        <div class="color-bar <%= loggedUserEmail.equals(player1Email) ? "white" : "black" %>"></div>
        <h3>${player}</h3>
    </div>
    <div class="surrender-button-container">
        <button class="surrender-button" onclick="openSurrenderPopup()">
            <i class="fas fa-flag"></i> Surrender
        </button>
    </div>
</div>
<div id="surrender-popup" class="popup">
    <div class="popup-content">
        <p>Are you sure you want to surrender?</p>
        <button onclick="confirmSurrender()">Yes</button>
        <button onclick="closeSurrenderPopup()">No</button>
    </div>
</div>
<script>
    var stompClient = null;

    function connect() {
        var socket = new SockJS('/game-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/game', function (message) {
                console.log('Message received: ', message);
                showMove(JSON.parse(message.body));
                location.reload();
            });
        });
    }

    function sendMove(move) {
        stompClient.send("/app/move", {}, JSON.stringify(move));
    }

    function showMove(move) {
        // Update the game board with the new move
        console.log(move);
    }

    connect();


    var selectedPawn = null;
    var gameId = "${game.id}";
    var currentPlayerId = "${game.currentPlayer.user.email}"
    var playerId = "${sessionScope.userEmail}"
    var opponentUsername = "${opponent}"
    var winner = "${game.winner}"
    var player1Email = "${game.player1.user.email}"
    var player2Email = "${game.player2.user.email}"

    function startUp() {
        initializeBoard()

        console.log("The winner is " + winner)

        if(winner === player1Email) {
            showWinnerPopup(winner);
        }

        if(winner === player2Email) {
            showWinnerPopup(winner);
        }

    }

    function initializeBoard() {
        console.log("Initializing board")
        console.log("Logged player id: " + playerId)
        console.log("Current player id: " + currentPlayerId)
        var squares = document.getElementsByClassName("square");
        for (var i = 0; i < squares.length; i++) {
            var square = squares[i];
            square.onclick = null; // Disable click events on all squares
            var piece = square.querySelector(".piece");
            if (piece !== null && piece.classList.contains("current_player") && currentPlayerId === playerId) {
                console.log("Setting functions")
                piece.onclick = handlePawnClick; // Enable click events only on squares containing white pawns
                piece.onmouseenter = handlePawnHover;
                piece.onmouseleave = clearPossibleMoves;
            }
        }

    }

    function handlePawnHover() {
        console.log("handling pawn hover")
        var row = this.parentElement.getAttribute("data-row");
        var col = this.parentElement.getAttribute("data-col");
        showPossibleMoves(row, col);
    }

    function showPossibleMoves(row, col) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/multiplayerPossibleMoves", true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        var pawn = {"startX": row, "startY": col, "endX": -1, "endY": -1, "gameId": gameId};
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var possibleMoves = JSON.parse(xhr.responseText);
                    highlightPossibleMoves(possibleMoves);
                } else {
                    // Handle error
                    alert("Error fetching possible moves!");
                }
            }
        };
        xhr.send(JSON.stringify(pawn));
    }

    function highlightPossibleMoves(possibleMoves) {
        clearPossibleMoves(); // Clear existing highlighted squares
        for (var i = 0; i < possibleMoves.length; i++) {
            var move = possibleMoves[i];
            var square = document.querySelector(".square[data-row='" + move.endX + "'][data-col='" + move.endY + "']");
            if (square) {
                square.classList.add("possible-move");
            }
        }
    }

    function clearPossibleMoves() {
        var possibleMoves = document.querySelectorAll(".possible-move");
        for (var i = 0; i < possibleMoves.length; i++) {
            possibleMoves[i].classList.remove("possible-move");
        }
    }

    function handlePawnClick() {
        var row = this.parentElement.getAttribute("data-row");
        var col = this.parentElement.getAttribute("data-col");
        selectedPawn = {row: row, col: col};
        enableEmptySquares();
    }

    function disableOtherPawns() {
        var squares = document.getElementsByClassName("square");
        for (var i = 0; i < squares.length; i++) {
            var piece = squares[i].querySelector(".piece");
            if (piece !== null && (squares[i].getAttribute("data-row") !== selectedPawn.row || squares[i].getAttribute("data-col") !== selectedPawn.col)) {
                piece.onclick = null; // Disable click events on other pawns
            }
        }
    }

    function enableEmptySquares() {
        var squares = document.getElementsByClassName("square");
        for (var i = 0; i < squares.length; i++) {
            if (squares[i].querySelector(".piece") === null) {
                squares[i].onclick = handleEmptySquareClick; // Enable click events on empty squares
            }
        }
    }

    function handleEmptySquareClick() {
        var targetRow = this.getAttribute("data-row");
        var targetCol = this.getAttribute("data-col");
        var move = {
            "startX": selectedPawn.row,
            "startY": selectedPawn.col,
            "endX": targetRow,
            "endY": targetCol,
            "gameId": gameId
        }

        //movePiece(selectedPawn.row, selectedPawn.col, targetRow, targetCol);
        sendMove(move);
        selectedPawn = null;
        initializeBoard();
    }

    function movePiece(startRow, startCol, endRow, endCol) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/multiplayerMove", false);
        xhr.setRequestHeader('Content-Type', 'application/json');
        var move = {"startX": startRow, "startY": startCol, "endX": endRow, "endY": endCol, "gameId": gameId};
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    console.log("Moved successfully!")
                    window.location.reload()
                    //makeComputerMove();
                } else {
                    alert("Move not allowed!");
                }
            }
        };
        console.log("Sending this move ==> " + JSON.stringify(move))
        xhr.send(JSON.stringify(move));
    }

    function makeComputerMove() {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "/computerMove", false);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var response = xhr.responseText;
                    if (response.includes("Player wins")) {
                        showWinnerPopup("player");
                    }

                    if (response.includes("Computer wins")) {
                        showWinnerPopup("computer");
                    }

                    if (response.includes("Your move, human.")) {
                        window.location.reload();
                    }
                } else {
                    console.error("Error getting computer move: " + xhr.responseText);
                }
            }
        };
        xhr.send();
    }

    function returnHome() {
        window.location.href = "/";
    }

    function restartGame() {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/restartGame", true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    window.location.reload();
                } else {
                    alert("Error resetting game!");
                }
            }
        };
        xhr.send();
    }

    function showWinnerPopup(winner) {
        var overlay = document.querySelector('.overlay');
        var popup = document.getElementById('popup');
        var winnerText = document.getElementById('winner-text');

        winnerText.innerText = "Winner: " + winner;
        overlay.style.display = "block";
        popup.style.display = "block";
    }

    function surrender(gameId) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/surrender?gameId=" + gameId, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    showWinnerPopup(opponentUsername);
                } else {
                    alert("Error surrendering!");
                }
            }
        };
        xhr.send();
    }

    function win(gameId) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/win?gameId=" + gameId, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    showWinnerPopup(opponentUsername);
                } else {
                    alert("Error processing win!");
                }
            }
        };
        xhr.send();
    }

    function openSurrenderPopup() {
        document.getElementById('surrender-popup').style.display = 'block';
    }

    function closeSurrenderPopup() {
        document.getElementById('surrender-popup').style.display = 'none';
    }

    function confirmSurrender() {
        surrender(gameId);
        closeSurrenderPopup();
    }
</script>
</body>
</html>
