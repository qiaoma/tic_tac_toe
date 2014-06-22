<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Who's Online (jQuery)</title>

<script type="text/javascript" src="javascript/jquery-2.1.1.min.js"></script>
<script type="text/javascript">
//method 1

$(function(){
	//jQuery.ajax() Perform an asynchronous HTTP (Ajax) request.
	//get the current list of users and populate the list	
	$.ajax({
			url: "wos.json",
			cash: false,
			success: function(data){
				//use data to populate the <ul>
				//for each username, create a <li>
				$("ul").empty();
				for(var i = 0; i < data.usernames.length; i++){				
					$("ul").append("<li>" + data.usernames[i] +"</li>");
					//console.log(data.usernames[i]);
				}			
			}
		});
	
	//get the updated list and populate the list
	updateList();	
});

function updateList(){
	
	$.ajax({
		url: "wos2.json",
		cash: false,
		success: function(data){
			//use data to populate the <ul>
			//for each username, create a <li>
			$("ul").empty();
			for(var i = 0; i < data.length; i++){				
				$("ul").append("<li>" + data[i] +"</li>");
				//console.log(data.usernames[i]);
			}
			updateList();
		}
	});
}

/*
method 2
$(function(){
	
	setInterval(function(){
		$.ajax({
			url: "wos.json",
			cash: false,
			success: function(data){
				//use data to populate the <ul>
				//for each username, create a <li>
				$("ul").empty();
				for(var i = 0; i < data.usernames.length; i++){
					
					$("ul").append("<li>" + data.usernames[i] +"</li>");
					//console.log(data.usernames[i]);
				}			
			}
		});
				
	}, 1000);
});
*/
</script>
</head>
<body>

<h3>Who's Online (jQuery)</h3>

<ul>	
</ul>

</body>
</html>