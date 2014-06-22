<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Game History</title>

</head>
<body>
<h2>Game History</h2>

<table border="1">
	<tr>
		<td>The number of games completed</td>
		<td>${numCompletedGames}</td>
	</tr>
	<tr>
		<td>The number of 1-player game completed</td>
		<td>${numCompletedGamesAI}</td>
	</tr>
	<tr>
		<td>The number of 2-player game completed</td>
		<td>${numCompletedGamesPlayer}</td>
	</tr>
	<tr>
		<td>The win rate against AI</td>
		<td>${winRateAI}</td>
	</tr>
	<tr>
		<td>The win rate against human players</td>
		<td>${winRatePlayer}</td>
	</tr>
</table>

<h3>The list of completed games played this month</h3>
	
<table border="1">
	<tr><th>Opponent's name</th><th>Game Length (seconds)</th><th>Outcome</th>
	<c:forEach items="${monthHistoryList}" var="monthHistory">
		<tr>
			<td>${monthHistory.opponentName}</td>
			<td>${monthHistory.gameLength}</td>
			<td>${monthHistory.outcome}</td>
		</tr>
	</c:forEach>
</table>

</body>
</html>