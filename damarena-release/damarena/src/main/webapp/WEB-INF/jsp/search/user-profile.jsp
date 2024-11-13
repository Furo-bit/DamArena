<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <link rel="icon" type="image/x-icon" href="/Images/logo_damarena.jpg">
    <link rel="stylesheet" type="text/css" href="/css/profile_style.css">

</head>
<body>
<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>

<div class="container">
    <h2>${userNow.username}</h2>

    <div class="profile-info">
        <c:choose>
            <c:when test="${empty profilePicture}">
                <img src="Images/default_user.jpg" alt="Profile Picture">
            </c:when>
            <c:otherwise>
                <img src="data:image/png;base64,${profilePicture}" alt="no">
            </c:otherwise>
        </c:choose>
        <div>
            <p><strong>Checkers Bullet ELO:</strong> ${userNow.eloScores['checkers-bullet']}</p>
            <p><strong>Checkers Blitz ELO:</strong> ${userNow.eloScores['checkers-blitz']}</p>
            <p><strong>Checkers Rapid ELO:</strong> ${userNow.eloScores['checkers-rapid']}</p>
        </div>
    </div>

    <h2>Friend list</h2>

    <table>
        <thead>
        <tr>
            <th>Username</th>
            <th>Checkers Bullet ELO</th>
            <th>Checkers Blitz ELO</th>
            <th>Checkers Rapid ELO</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="friend" items="${friends}">
            <tr style="cursor: pointer;" onclick="window.location='/user-profile?userId=${friend.email}'">
                <td>${friend.username}</td>
                <td>${friend.eloScores['checkers-bullet']}</td>
                <td>${friend.eloScores['checkers-blitz']}</td>
                <td>${friend.eloScores['checkers-rapid']}</td>
                <c:choose>
                    <c:when test="${isLoggedin && email ne friend.email}">
                        <td>
                            <c:choose>
                                <c:when test="${friendMap[friend.email]}">
                                    <form action="/unfollow" method="post">
                                        <input type="hidden" name="userId" value="${email}"/>
                                        <input type="hidden" name="friendId" value="${friend.email}"/>
                                        <button class="unfollow-button" style="background-color: red;" type="submit">
                                            Unfollow
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="/add-friend" method="post">
                                        <input type="hidden" name="userId" value="${email}"/>
                                        <input type="hidden" name="friendId" value="${friend.email}"/>
                                        <button class="unfollow-button" style="background-color: #2196F3;" type="submit">
                                            Follow
                                        </button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td></td>
                    </c:otherwise>
                </c:choose>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
