<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style>
	.button {
		background-color: lightblue;
		border: 2px solid lightblue;
		color: white;
		padding: 15px 32px;
		text-align: center;
		text-decoration: none;
		display: inline-block;
		font-size: 20px;
		font-weight: bold;
		border-radius: 4px;
		cursor: pointer;
		box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
	}
	
	.button:hover {
  		background-color: white;
		color: lightblue;
		text-align: center;
		text-decoration: none;
		display: inline-block;
		border-radius: 4px;
		box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
	}

	.center2 {
		position: absolute;
  		top: 50%;
  		left: 50%;
  		transform: translate(-50%, -50%);
	}

  	body {
 		background-image: url("css/geisel2.jpg");
 		background-position: relative; /* Center the image */
  		background-repeat: no-repeat; /* Do not repeat the image */
  		background-size: cover; /* Resize the background image to cover the entire container */
	}  
	
	h1 {
		color: white;
		font-size: 50px
	}
	
	td {
		color: white;
	}
	
	#tritonGIF img {
    	position: absolute;
    	top: 0px;
    	left: 0px;
    	width: 105px;
    	height: 150px;
    	
	}	
	
	#welcomeGIF img{
		position: absolute;
    	top: 50%;
    	left: 50%;
	}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Main Page</title>
</head>
<body>
	
<div id="tritonGIF">
    <img src="css/tritonGIF.gif" class="ribbon"/>
</div>
	
<div class="center2" align="center">
<h1>Welcome Back! Tritons!</h1>
<img id="welcomeGIF" src="css/welcome.gif " class="ribbon"/>
<form action="<%= request.getContextPath() %>/UserController" method="get">
<br/>
<input type="submit" class="button" value="Sign In"/>
</form>
</div>
</body>
</html>