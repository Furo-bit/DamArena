<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="playerId" value="${sessionScope.userEmail}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/x-icon" href="/Images/logo_damarena.jpg">
    <link rel="stylesheet" type="text/css" href="/css/search_style.css">
    <link rel="stylesheet" type="text/css" href="/css/index.css">
    <link rel="stylesheet" type="text/css" href="/css/tournaments_style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <title>Tournaments</title>
</head>
<body>
<div class="container">
    <header>
        <div class="return-link">
            <a href="/">
                <img class="return-image" src="../../../Images/damarena.svg" alt="Home" width="100" height="100">
            </a>
        </div>
    </header>

    <div class="container-tournament" style="width: 1200px;">
        <div class="tournament-header">
            <h2>Tournament: ${tournament.name} (${tournament.format})</h2>
            <c:if test="${joined}">
                <form action="/tournaments/leave" method="post" style="display:inline;">
                    <input type="hidden" name="tournamentId" value="${tournament.id}">
                    <input type="hidden" name="playerId" value="${playerId}">
                    <button type="submit" class="leave-button"
                            <c:if test="${tournament.isInProgress || tournament.isEnded}">disabled</c:if>>
                        <i class="fas fa-sign-out-alt"></i> Leave
                    </button>
                </form>
            </c:if>
            <c:if test="${!joined}">
                <form action="/tournaments/join" method="post" style="display:inline;">
                    <input type="hidden" name="tournamentId" value="${tournament.id}">
                    <input type="hidden" name="playerId" value="${playerId}">
                    <button type="submit" class="join-button"
                            <c:if test="${tournament.isPrivate || playerId == null || tournament.players.size() == tournament.maxNumPlayers || tournament.isInProgress || tournament.isEnded}">disabled</c:if>>
                        <i class="fas fa-sign-in-alt"></i> Join
                    </button>
                </form>
            </c:if>
        </div>

        <p>Starting time: ${tournament.startingTime.hour}:${tournament.startingTime.minute}
            ${tournament.startingTime.dayOfMonth}-${tournament.startingTime.monthValue}-${tournament.startingTime.year}</p>
        <p>Time remaining: <span id="countdown"></span></p>

        <c:if test="${tournament.winner != null}">
            <div class="winner-container">
                <h3>The winner is: ${tournament.winner.username}</h3>
            </div>
        </c:if>

        <c:if test="${not tournament.isEnded}">
            <div style="width: 70%">
                <table class="PlayersTable">
                    <thead>
                    <tr>
                        <th colspan="2">Participants</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty tournament.players}">
                        <tr>
                            <td>No participants</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty tournament.players}">
                        <c:forEach var="player" items="${players}">
                            <tr>
                                <td>${player.username}</td>
                                <c:choose>
                                    <c:when test="${!(tournament.isEnded || tournament.isInProgress) && playerId.equals(tournament.owner.email) && !playerId.equals(player.email)}">
                                        <td>
                                            <form action="/tournaments/removePlayer" method="post"
                                                  style="display:inline;"
                                                  onsubmit="return confirmRemoval(this);">
                                                <input type="hidden" name="tournamentId" value="${tournament.id}">
                                                <input type="hidden" name="playerId" value="${player.email}">
                                                <button type="submit" class="remove-button">
                                                    <i class="fas fa-user-times"></i> Remove
                                                </button>
                                            </form>
                                        </td>
                                    </c:when>
                                    <c:when test="${player.equals(tournament.owner)}">
                                        <td>Owner</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>

</div>

<script>

    const tournamentStartingTime = new Date(
        ${tournament.startingTime.year},
        ${tournament.startingTime.monthValue - 1},
        ${tournament.startingTime.dayOfMonth},
        ${tournament.startingTime.hour},
        ${tournament.startingTime.minute},
        0,
        0
    ).getTime();

    function updateCountdown() {
        const now = new Date().getTime();
        const distance = tournamentStartingTime - now;

        const days = Math.floor(distance / (1000 * 60 * 60 * 24));
        const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((distance % (1000 * 60)) / 1000);

        const countdownElement = document.getElementById("countdown");
        countdownElement.innerHTML = days + 'd ' + hours + 'h ' + minutes + 'm ' + seconds + 's';

        if (distance <= 60000) {
            countdownElement.style.color = "red";
        } else {
            countdownElement.style.color = "black";
        }

        if (distance < 0) {

            clearInterval(countdownInterval);
            if (${isInProgress}) {
                countdownElement.innerHTML = "IN PROGRESS";
                countdownElement.style.color = "green";
            } else {
                countdownElement.innerHTML = "ENDED";
                countdownElement.style.color = "black";
            }

            document.getElementById("joinButton").disabled = true;
        }
    }

    function confirmRemoval(form) {
        return confirm("Are you sure you want to remove this player?");
    }

    const countdownInterval = setInterval(updateCountdown, 1000);
    updateCountdown();

</script>
</body>
</html>
