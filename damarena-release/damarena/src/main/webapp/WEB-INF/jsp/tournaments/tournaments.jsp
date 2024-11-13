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
    <link rel="stylesheet" type="text/css" href="/css/rankings_style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <title>Tournaments</title>
</head>
<body>


<script>
    const currentPlayerId =
    ${playerId}
</script>

<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>

<div class="rankings-body">
    <div class="container-game-mode" style="width: 70%;">
        <div class="tournament-header">
            <h2>On Going Tournaments</h2>
            <c:if test="${isLoggedin}">
                <button class="join-button" onclick="window.location='/tournaments/new-tournament'">Add New Tournament</button>
            </c:if>
        </div>
        <table>
            <thead>
            <tr>
                <th>Tournaments</th>
                <th>Starting Time</th>
                <th>Format</th>
                <th>Players</th>
                <th>Owner</th>
                <th>Privacy</th>
                <th>Join</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="tournament" items="${tournaments}">
                <tr style="cursor: pointer;" onclick="window.location='/tournaments/tournament/${tournament.id}'">
                    <td>${tournament.name}</td>
                    <td>${tournament.startingTime.hour}:${tournament.startingTime.minute}
                            ${tournament.startingTime.dayOfMonth}-${tournament.startingTime.monthValue}-${tournament.startingTime.year}</td>
                    <td>${tournament.format}</td>
                    <td>${tournament.players.size()}/${tournament.maxNumPlayers}</td>
                    <td>${tournament.owner.username}</td>
                    <td>${tournament.isPrivate ? 'Private' : 'Public'}</td>
                    <td>
                        <c:if test="${joinedTournaments.contains(tournament.id)}">
                            <form action="/tournaments/leave" method="post" style="display:inline;">
                                <input type="hidden" name="tournamentId" value="${tournament.id}">
                                <input type="hidden" name="playerId" value="${playerId}">
                                <button type="submit" class="leave-button">
                                    <i class="fas fa-sign-out-alt"></i> Leave
                                </button>
                            </form>
                        </c:if>
                        <c:if test="${!joinedTournaments.contains(tournament.id)}">
                            <form action="/tournaments/join" method="post" style="display:inline;">
                                <input type="hidden" name="tournamentId" value="${tournament.id}">
                                <input type="hidden" name="playerId" value="${playerId}">
                                <button type="submit" class="join-button"
                                        <c:if test="${tournament.isPrivate || playerId == null || tournament.players.size() == tournament.maxNumPlayers || tournament.isInProgress}">disabled</c:if>>
                                    <i class="fas fa-sign-in-alt"></i> Join
                                </button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="container-game-mode" style="width: 30%;">
        <div class="tournament-header">
            <h2>Ended Tournaments</h2>
        </div>

        <table>
            <thead>
            <tr>
                <th>Tournament</th>
                <th>Winner</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="tournament" items="${ended_tournaments}">
                <tr style="cursor: pointer;" onclick="window.location='/tournaments/tournament/${tournament.id}'">
                    <td>${tournament.name}</td>
                    <td style="color: goldenrod">${tournament.winner.username}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>


</body>
</html>
