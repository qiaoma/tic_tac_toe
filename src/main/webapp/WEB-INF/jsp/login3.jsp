<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h2>Login</h2>

<form name="login" method="post" action="<c:url value='/j_spring_security_check' />">
  
    <table>
        <tr>
          <td>User:</td>
          <td><input type="text" name="j_username"></td>
        </tr>
        <tr>
           <td>Password:</td>
           <td><input type="password" name="j_password" /></td>
        </tr>
        <tr>
           <td><input name="submit" type="submit" value="Submit" /> </td>
        </tr>
    </table>
  
</form>
</body>
</html>