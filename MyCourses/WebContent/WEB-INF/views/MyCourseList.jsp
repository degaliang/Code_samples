<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="user.model.User"%>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="courses.model.Course" %>

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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"/>
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    	<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.6.3/jquery-ui-timepicker-addon.min.js"></script>
        <script>
            $(function () {
                $("#datepicker").datetimepicker();
            });
            
        </script>
        <script>
       		$(document).on("load", function() {  
            	$.get("ListUserCourse");
        	});
        </script>
        <script>
		function showdv(obj, id1, id2)
            {
            	txt=obj.options[obj.selectedIndex].text; 
            	document.getElementById(id1).style.display='none';

            	if(txt.match(id2))
            	{
            	document.getElementById(id1).style.display='table-row';
            	}
            }
		</script>
		<script src="../../convertTime.js"></script>
<title>My Courses</title>
</head>
<body>
<div id="triton">
    <img src="css/triton2.png" class="ribbon"/>
</div>
 
<div>
<%

try {
Class.forName("com.mysql.jdbc.Driver");
} catch (ClassNotFoundException e) {
e.printStackTrace();
}

Connection connection = null;
Statement statement = null;
ResultSet resultSet = null;

%>
<% User user = (User)session.getAttribute("user"); 
	String userName = user.getUserName();
%>
<h1 align="center"><font><strong>My Current Courses<%=userName %></strong></font></h1>
<table align="center" cellpadding="5" cellspacing="5" border="1">
<tr>

</tr>
<tr bgcolor="lightblue">
<td><b>Course Name</b></td>
<td><b>Weekly Quiz</b></td>
<td><b>Weekly Homework</b></td>
<td><b>Reading Quiz</b></td>
<td><b>Final Date</b></td>
</tr>


<%
try{ 
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courses", "root", "Ldf010217");
statement=connection.createStatement();
String sql ="SELECT * FROM courses.courses";
resultSet = statement.executeQuery(sql);
while(resultSet.next()){
%>
<tr>

<td><%=resultSet.getString("course_name") %></td>
<td><%=resultSet.getString("weekly_quiz") %></td>
<td><%=resultSet.getString("weekly_hw") %></td>
<td><%=resultSet.getString("reading_quiz") %></td>
<td><%=resultSet.getString("final_date") %></td>

<%
	String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+
    				  ":"+request.getServerPort()+path+"/";
%>


<td>
	<form action="view" method="post">
	<input type="hidden" class="button" name="courseName" value="<%=resultSet.getString("course_name") %>" />
	<input type="submit" class="button" value="View" />
	</form>
</td>
<td>
	<form action="edit" method="post">
	<input type="hidden" class="button" name="courseName" value="<%=resultSet.getString("course_name") %>" />
	<input type="submit" class="button" value="Edit" />
	</form>
</td>
<td>
	<form action="remove" method="post"> 
	<input type="hidden" class="button" name="courseName" value="<%=resultSet.getString("course_name") %>" />
	<input type="submit" class="button" value="Remove" />
	</form> 
</td>
</tr>

<% 
}
} catch (Exception e) {
e.printStackTrace();
}
%>

</table>
<div align="center">
<br />
	<form action="add" method="post">
  	<input type="submit" class="button" value="Add a new course" />
  	<input type="button" class="button" value="Back to Main Page" onclick="location.href='MainPage.jsp'" />
  	</form>
</div>

</div>
 
</body>
</html>