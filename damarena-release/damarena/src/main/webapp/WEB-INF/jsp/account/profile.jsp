<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <link rel="icon" type="image/x-icon" href="/Images/logo_damarena.jpg">
    <link rel="stylesheet" type="text/css" href="/css/profile_style.css">
    <link rel="stylesheet" type="text/css" href="/css/game_mode_style.css">
</head>
<body>
<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>

<div class="container">
    <h2>${user.username}</h2>

    <div class="settings">
        <img id="dotsIcon" src="/Images/three-dots-menu.svg" alt="Menu" width="25" height="50">
        <div id="user-settings" class="settings-menu">
            <form action="/delete-user" method="post">
                <input type="hidden" name="userId" value="${user.email}"/>
                <button type="submit">Delete account</button>
            </form>
        </div>
    </div>

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

            <p><strong>Checkers Bullet ELO:</strong> ${user.eloScores['checkers-bullet']}</p>
            <p><strong>Checkers Blitz ELO:</strong> ${user.eloScores['checkers-blitz']}</p>
            <p><strong>Checkers Rapid ELO:</strong> ${user.eloScores['checkers-rapid']}</p>
        </div>
    </div>

    <div class="profile-info">
        <form id="uploadForm" action="/profile/uploadPicture" method="post" enctype="multipart/form-data">
            <input type="file" name="file" id="fileInput" style="display: none;" accept="image/*">
            <button class="profile-info-button" type="button" id="uploadButton">
                Change pic
            </button>
        </form>
    </div>
    <h2>Friend list</h2>

    <table>
        <thead>
        <tr>
            <th>Username</th>
            <th style="text-align: center">Checkers Bullet ELO</th>
            <th style="text-align: center">Checkers Blitz ELO</th>
            <th style="text-align: center">Checkers Rapid ELO</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${not empty friends}">
            <c:forEach var="friend" items="${friends}">
                <tr style="cursor: pointer;" onclick="window.location='/user-profile?userId=${friend.email}'">
                    <td>${friend.username}</td>
                    <td style="text-align: center">${friend.eloScores['checkers-bullet']}</td>
                    <td style="text-align: center">${friend.eloScores['checkers-blitz']}</td>
                    <td style="text-align: center">${friend.eloScores['checkers-rapid']}</td>
                    <td class="unfollow-button-td">
                        <form action="/unfollow" method="post">
                            <input type="hidden" name="userId" value="${email}"/>
                            <input type="hidden" name="friendId" value="${friend.email}"/>
                            <button style="background-color: red; width: 100px;" type="submit">Unfollow</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </c:if>

        </tbody>
    </table>
</div>
<script>
    var fileInput = document.getElementById('fileInput');
    var uploadButton = document.getElementById('uploadButton');

    uploadButton.addEventListener('click', function () {
        if (fileInput.style.display === 'none') {
            fileInput.click();
        } else {
            document.getElementById('uploadForm').submit();
        }
    });

    fileInput.addEventListener('change', function () {

        fileInput.style.display = 'none';

        uploadButton.innerText = 'Uploading...';

        document.getElementById('uploadForm').submit();
    });

    document.getElementById('dotsIcon').addEventListener('click', function () {
        var userSettings = document.getElementById('user-settings');

        if (userSettings.style.display === 'none' || userSettings.style.display === '') {
            userSettings.style.display = 'block';
        } else {
            userSettings.style.display = 'none';
        }
    });
</script>
</body>
</html>
