<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search Results</title>
    <link rel="icon" type="image/x-icon" href="/Images/logo_damarena.jpg">
    <link rel="stylesheet" type="text/css" href="/css/search_style.css">
    <link rel="stylesheet" type="text/css" href="/css/create_account_style.css">
    <link rel="stylesheet" type="text/css" href="/css/index.css">

</head>
<body>
<div class="return-link">
    <a href="/">
        <img class="return-image" src="Images/damarena.svg" alt="Home" width="100" height="100">
    </a>
</div>
<div class="container-game-mode">
    <h2>Search results:</h2>
    <table>
        <thead>
        <tr>
            <th>Nickname</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${users}">
            <c:if test="${user.email ne email}">
                <tr>
                    <td onclick="window.location='/user-profile?userId=${user.email}'">${user.username}</td>
                    <td>
                        <c:choose>
                            <c:when test="${friendMap[user.email]}">
                                <form action="/unfollow" method="post">
                                    <input type="hidden" name="userId" value="${email}"/>
                                    <input type="hidden" name="friendId" value="${user.email}"/>
                                    <button style="background-color: red;" type="submit">Unfollow</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${isLoggedin}">
                                    <form action="/add-friend" method="post">
                                        <input type="hidden" name="userId" value="${email}"/>
                                        <input type="hidden" name="friendId" value="${user.email}"/>
                                        <button type="submit">Follow</button>
                                    </form>
                                </c:if>
                                <c:if test="${not isLoggedin}">
                                    <button class="follow-button" disabled>Follow</button>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>