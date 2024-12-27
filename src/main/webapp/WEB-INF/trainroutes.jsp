<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Управление маршрутами поездов</title>
</head>
<body>
<h1>Управление маршрутами поездов</h1>

<h2>Список маршрутов</h2>
<table border="1">
    <tr>
        <th>Номер поезда</th>
        <th>Название маршрута</th>
        <th>Действия</th>
    </tr>
    <c:forEach var="route" items="${routes}">
        <tr>
            <td>${route.trainNumber}</td>
            <td>${route.routeName}</td>
            <td>
                <form action="${pageContext.request.contextPath}/" method="get" style="display: inline;">
                    <input type="hidden" name="trainNumber" value="${route.trainNumber}">
                    <input type="submit" value="Редактировать">
                </form>
                <form action="${pageContext.request.contextPath}/" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="trainNumber" value="${route.trainNumber}">
                    <input type="submit" value="Удалить">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<h2>${route != null ? "Редактировать маршрут" : "Добавить новый маршрут"}</h2>
<form action="${pageContext.request.contextPath}/" method="post">
    <input type="hidden" name="action" value="${route != null ? 'edit' : 'add'}">
    <label for="trainNumber">Номер поезда:</label>
    <input type="text" id="trainNumber" name="trainNumber" value="${route != null ? route.trainNumber : ''}" ${route != null ? 'readonly' : ''} required><br>
    <label for="routeName">Название маршрута:</label>
    <input type="text" id="routeName" name="routeName" value="${route != null ? route.routeName : ''}" required><br>
    <input type="submit" value="${route != null ? 'Обновить' : 'Добавить'}">
</form>
</body>
</html>
