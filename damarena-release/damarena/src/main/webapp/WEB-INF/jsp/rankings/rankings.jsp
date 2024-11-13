<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Rankings</title>
    <link rel="stylesheet" type="text/css" href="/css/index.css">
    <link rel="stylesheet" type="text/css" href="/css/rankings_style.css">
    <script>
        function sortTable(columnIndex) {
            var table = document.getElementById("rankingsTable");
            var rows = Array.from(table.rows).slice(1);
            var isNumericColumn = columnIndex !== 1;

            rows.sort(function (a, b) {
                var cellA = a.cells[columnIndex].innerText;
                var cellB = b.cells[columnIndex].innerText;

                if (isNumericColumn) {
                    return parseInt(cellB) - parseInt(cellA);
                } else {
                    return cellA.localeCompare(cellB);
                }
            });

            rows.forEach(function (row, index) {
                row.cells[0].innerText = index + 1;
                table.appendChild(row);
            });
        }
    </script>
</head>
<body>
<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>

<div class="rankings-body">
    <div class="container-game-mode">
        <h2 class="rankings-header">Checkers ELO Rankings</h2>
        <table id="rankingsTable">
            <thead>
            <tr>
                <th>Rank</th>
                <th>Username</th>
                <th style="cursor: pointer;" onclick="sortTable(2)">Bullet</th>
                <th style="cursor: pointer;" onclick="sortTable(3)">Blitz</th>
                <th style="cursor: pointer;" onclick="sortTable(4)">Rapid</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="user" varStatus="status">
                <tr class="${currentUser != null && currentUser.username.equals(user.username) ? 'highlight' : ''}"
                    style="cursor: pointer;" onclick="window.location='/user-profile?userId=${user.email}'">
                    <td>${status.index + 1}</td>
                    <td>${user.username}</td>
                    <td>${user.eloScores['checkers-bullet']}</td>
                    <td>${user.eloScores['checkers-blitz']}</td>
                    <td>${user.eloScores['checkers-rapid']}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="container-game-mode">
        <h2 class="rankings-header">Checkers Tournament Winners</h2>
        <table>
            <thead>
            <tr>
                <th>Tournament</th>
                <th>Winner</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="tournament" items="${tournaments}">
                <tr style="cursor: pointer;" onclick="window.location='/tournaments/tournament/${tournament.id}'">
                    <td>${tournament.name}</td>
                    <td style="color: darkgoldenrod">${tournament.winner.username}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
