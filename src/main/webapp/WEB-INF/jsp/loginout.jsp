<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login and Logout Page</title>

</head>
<body>

<form action="whoslogin.html" method="get">	
	<p>
		Username: <input type="text" name="username"/>
		<input type="submit" name="login" value="Login"/>
	</p>
</form>
<form action="whoslogout.html" method="get">	
	<p>
		Username: <input type="text" name="username"/>
		<input type="submit" name="logout" value="Logout"/>
	</p>
</form>

<a href="whosonline1.html">Who's online</a><br/>

</body>
</html>