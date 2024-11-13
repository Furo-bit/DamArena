<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>New Tournament</title>
    <link rel="stylesheet" type="text/css" href="/css/index.css">
    <link rel="stylesheet" type="text/css" href="/css/create_account_style.css">

</head>
<body>
<div class="return-link">
    <a href="/">
        <img class="return-image" src="/Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>
<div class="wrapper">
    <div class="container" style="position: absolute">
        <h2>Create a new Tournament</h2>
        <form action="/new-tournament" method="post">

            <div class="form-group">
                <label for="name">Name:</label>
                <input style="width: 100%" type="text" name="name" id="name">
            </div>

            <div class="inline-group">
                <label for="format">Type:</label>
                <select style="width: 160px" name="format" id="format">
                    <option value="Bullet">Bullet</option>
                    <option value="Blitz">Blitz</option>
                    <option value="Rapid">Rapid</option>
                </select>
            </div>

            <div class="inline-group">
                <label for="maxNumPlayers">Maximum players:</label>
                <input style="width: 160px" type="number" name="maxNumPlayers" id="maxNumPlayers">
            </div>

            <div class="inline-group">
                <label for="startingTime">Starting Time:</label>
                <input style="width: 160px" type="datetime-local" name="startingTime" id="startingTime">
            </div>

            <div class="inline-group" style="justify-content: left">
                <label for="isPrivate">Private:</label>
                <input type="checkbox" name="isPrivate" id="isPrivate">
            </div>

            <div class="form-group">
                <button id="toggleButton" onclick="toggleTable(event)">Invite Friends</button>
            </div>

            <div class="friend-popup-wrapper">
                <div id="friendList">
                    <table class="friendTable">
                        <thead>
                        <tr>
                            <th colspan="2">Select friends to invite</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${empty friends}">
                                <tr>
                                    <td colspan="2">You have no friends...</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="friend" items="${friends}">
                                    <tr>
                                        <td style="width: 20px">
                                            <input type="checkbox" name="selectedFriends" id="selectedFriends"
                                                   value="${friend.email}">
                                        </td>
                                        <td style="text-align: left">${friend.username}</td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>

            <input type="submit" id="submitButton" value="Create Tournament">
        </form>
    </div>

</div>
<script>
    function toggleTable(event) {
        event.preventDefault();
        var friendList = document.getElementById('friendList');
        if (friendList.style.display === 'none' || friendList.style.display === '') {
            friendList.style.display = 'block';
        } else {
            friendList.style.display = 'none';
        }
    }
</script>
</body>
</html>
