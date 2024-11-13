<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.project.damarena.model.Board" %>
<!DOCTYPE html>
<html>
<head>
    <title>Board</title>
    <link rel="stylesheet" type="text/css" href="/css/board_style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">

    <script>
        var selectedPawn = null;

        function initializeBoard() {
            var squares = document.getElementsByClassName("square");
            for (var i = 0; i < squares.length; i++) {
                var square = squares[i];
                square.onclick = null;
                var piece = square.querySelector(".piece");
                if (piece !== null && (piece.classList.contains("player_color") || piece.classList.contains("player_color_king"))) {
                    piece.onclick = handlePawnClick;
                    piece.onmouseenter = handlePawnHover;
                    piece.onmouseleave = clearPossibleMoves;
                }
            }

        }

        function handlePawnHover() {
            var row = this.parentElement.getAttribute("data-row");
            var col = this.parentElement.getAttribute("data-col");
            showPossibleMoves(row, col);
        }

        function showPossibleMoves(row, col) {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/possibleMoves", true);
            xhr.setRequestHeader('Content-Type', 'application/json');
            var pawn = {"row": row, "col": col};
            xhr.onreadystatechange = function () {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        var possibleMoves = JSON.parse(xhr.responseText);
                        highlightPossibleMoves(possibleMoves);
                    } else {
                        alert("Error fetching possible moves!");
                    }
                }
            };
            xhr.send(JSON.stringify(pawn));
        }

        function highlightPossibleMoves(possibleMoves) {
            clearPossibleMoves();
            for (var i = 0; i < possibleMoves.length; i++) {
                var move = possibleMoves[i];
                var square = document.querySelector(".square[data-row='" + move.endRow + "'][data-col='" + move.endCol + "']");
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
                    piece.onclick = null;
                }
            }
        }

        function enableEmptySquares() {
            var squares = document.getElementsByClassName("square");
            for (var i = 0; i < squares.length; i++) {
                if (squares[i].querySelector(".piece") === null) {
                    squares[i].onclick = handleEmptySquareClick;
                }
            }
        }

        function handleEmptySquareClick() {
            var targetRow = this.getAttribute("data-row");
            var targetCol = this.getAttribute("data-col");
            movePiece(selectedPawn.row, selectedPawn.col, targetRow, targetCol);
            selectedPawn = null;
            initializeBoard();
        }

        function movePiece(startRow, startCol, endRow, endCol) {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/playerMove", false);
            xhr.setRequestHeader('Content-Type', 'application/json');
            var move = {"startRow": startRow, "startCol": startCol, "endRow": endRow, "endCol": endCol};
            xhr.onreadystatechange = function () {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        makeComputerMove();
                    } else {
                        alert("Move not allowed!");
                    }
                }
            };
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
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/restartGame", true);
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.onreadystatechange = function () {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        window.location.href = "/";
                    } else {
                        alert("Error resetting game!");
                    }
                }
            };
            xhr.send();
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

        function surrender() {
            showWinnerPopup("computer");
        }

        function openSurrenderPopup() {
            document.getElementById('surrender-popup').style.display = 'block';
        }

        function closeSurrenderPopup() {
            document.getElementById('surrender-popup').style.display = 'none';
        }

        function confirmSurrender() {
            surrender();
            closeSurrenderPopup();
        }
    </script>
</head>
<body onload="initializeBoard()">

<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>
<div class="overlay"></div>
<div id="popup" class="popup">
    <div id="winner-text" class="winner-text"></div>
    <button onclick="returnHome()">Return Home</button>
    <button onclick="restartGame()">Restart Game</button>
</div>

<div class="board-container">
    <div class="board">
        <%
            char[][] boardArray = (char[][]) request.getAttribute("board");
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    char piece = boardArray[row][col];
                    String pieceClass = "";
                    if (piece != Board.EMPTY && piece != Board.NOT_VALID) {
                        if (piece == Board.PLAYER_COLOR) {
                            pieceClass = "player_color";
                            if (Board.PLAYER_COLOR == 'w') {
                                pieceClass += " white";
                            } else {
                                pieceClass += " black";
                            }
                        } else if (piece == Board.PLAYER_COLOR_KING) {
                            pieceClass = "player_color_king";
                            if (Board.PLAYER_COLOR == 'w') {
                                pieceClass += " white";
                            } else {
                                pieceClass += " black";
                            }
                        } else if (piece == Board.OPPONENT_COLOR) {
                            pieceClass = "opponent_color";
                            if (Board.OPPONENT_COLOR == 'w') {
                                pieceClass += " white";
                            } else {
                                pieceClass += " black";
                            }
                        } else if (piece == Board.OPPONENT_COLOR_KING) {
                            pieceClass = "opponent_color_king";
                            if (Board.OPPONENT_COLOR == 'w') {
                                pieceClass += " white";
                            } else {
                                pieceClass += " black";
                            }
                        }
                    }
        %>
        <div class="square <%= (row + col) % 2 == 0 ? "even" : "odd" %>" data-row="<%= row %>" data-col="<%= col %>">
            <% if (piece != Board.EMPTY && piece != Board.NOT_VALID) { %>
            <div class="piece <%= pieceClass %>"></div>
            <% } %>
        </div>
        <%
                }
            }
        %>
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

</body>
</html>
