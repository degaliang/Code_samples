<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style>
	.button {
		background-color: lightblue;
		border: 2px solid lightblue;;
		color: white;
		text-align: center;
		text-decoration: none;
		display: inline-block;
		border-radius: 4px;
		cursor: pointer;
		box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
  		font-size: 16px;
  		margin: 4px 2px;
	}
	
	.button:hover {
  		background-color: white;
		color: lightblue;
	}

  	body {
 		background-image: url("css/geisel2.jpg");
 		background-position: relative; /* Center the image */
  		background-repeat: no-repeat; /* Do not repeat the image */
  		background-size: cover; /* Resize the background image to cover the entire container */
	}  
	
	h1, h2 {
		color: lightblue;
	}
	
	td {
		color: white;
		font-weight: bold;
	}
	
	p {
		color: white;
		font-weight: bold;
	}
	
	#triton img{
		position: absolute;
    	top: 0px;
    	left: 0px;
    	width: 13%;
    	height: 13%;
	}
</style>
<title>Login</title>
</head>
<body>
	<div align="center">
		<h1>Sign In</h1>
		<form action="login" method="post">
			<table>
				<tr>
					<td>Username: </td>
					<td><input type="text" name="userName" /></td>
				</tr>
				<tr>
					<td>Password: </td>
					<td><input type="password" name="passWord" /></td>
				</tr>
			</table>
			<%
				String login_msg=(String)request.getAttribute("err_msg");  
				if(login_msg!=null)
				out.println("<font color=red size=3px>"+login_msg+"</font>");
			%>
			<br />
			<input type="submit" class="button" value="Sign In" />
		</form>
		<form action="register" method="post">
			<p>
			Don't have an account?
			<input type="submit" class="button" value="Register" />
			</p>
		</form>
		<input type="button" class="button" value="Back to Main Page" onclick="location.href='MainPage.jsp'" />
	</div>
</body>
</html>