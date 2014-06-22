<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>

<style type="text/css">
	body{
        background-color:#FFFF99;
    }
	h2{
      color: 663300;
    }
</style>

</head>
<body>

<h2>Register</h2>

<form:form modelAttribute="user">
    <table>
        <tr>
            <td>Username:</td>
            <td><form:input path="username" />
            	<b><form:errors path="username" /></b>
            </td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><form:input path="password" type="password"/>
            	<b><form:errors path="password" /></b>
            </td>
        </tr>
       <tr>
            <td>Email:</td>
            <td><form:input path="email" />
            	<b><form:errors path="email" /></b>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Submit" />
            </td>
        </tr>
    </table>
</form:form>

</body>
</html>