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
		color: lightblue;
		font-weight: bold;
	}
	
	.title {
		color: white;
		font-weight: bold;
	}
	
	.div1 {
		position: absolute;
  		top: 50%;
  		left: 50%;
  		transform: translate(-50%, -50%);
	}
	
	#triton img{
		position: absolute;
    	top: 0px;
    	left: 0px;
    	width: 13%;
    	height: 13%;
	}	
	
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%=request.getParameter("courseName") %></title>
</head>
<body>
<div id="triton">
    <img src="css/triton2.png" class="ribbon"/>
</div>


	<div class="div1">
		<h1 align="center"><%=request.getParameter("courseName") %></h1>
		<table align="center" cellpadding="5" cellspacing="5" border="1">
		<tr>

		</tr>
		<tr bgcolor="lightblue">
		<td class="title"><b>Assignments</b></td>
		<td class="title"><b>Due Date</b></td>
		<td class="title"><b>Details</b></td>
		</tr>
		<% 
		try {
		Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		e.printStackTrace();
		}

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try{ 
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courses", "root", "Ldf010217");
		statement=connection.createStatement();
		String sql = "SELECT * FROM courses WHERE course_name = '" + request.getParameter("courseName") + "'";
		resultSet = statement.executeQuery(sql);
		} catch (Exception e) {
		e.printStackTrace();
		}
		while(resultSet.next()){
		%>
		<tr bgcolor="white">

		<td>Reading Quiz</td>
		<td><%=resultSet.getString("reading_due") %></td>
		<td><%=resultSet.getString("rd_details") %></td>

        </tr>
        <tr bgcolor="white">

		<td>Weekly Quiz</td>
		<td><%=resultSet.getString("quiz_time") %></td>
		<td><%=resultSet.getString("quiz_details") %></td>

        </tr>
        <tr bgcolor="white">

		<td>Weekly HW</td>
		<td><%=resultSet.getString("hw_due") %></td>
		<td><%=resultSet.getString("hw_details") %></td>

        </tr>
        <%
		}
        %>
		</table>
		
		<br />
		
		<div align="center">
		<input type="button" class="button" value="Back" onclick="history.back()" />
		</div>
	</div>
</body>
</html>

