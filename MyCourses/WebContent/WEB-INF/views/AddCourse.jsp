<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="user.model.User"%>
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
	
	#triton img{
		position: absolute;
    	top: 0px;
    	left: 0px;
    	width: 13%;
    	height: 13%;
	}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.6.3/jquery-ui-timepicker-addon.min.css"/>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"/>
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    	<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.6.3/jquery-ui-timepicker-addon.min.js"></script>
        <script src="js/moment.js"></script>
		<script src="js/moment-timezone-with-data.js"></script>
		<script type="text/javascript">
			console.log(moment()); 
		</script>
        <script>
            $(function () {
                $("#datepicker").datetimepicker({
                	dateFormat: 'yy-mm-dd',
                    timeFormat: 'HH:mm',
                    showSecond: false,
                    showMillisec: false,
                    showMicrosec: false,
                });
            });
            
        </script>
        <script>
		function showdv(obj, id1, id2)
            {
            	var txt = obj.options[obj.selectedIndex].text; 
            	document.getElementById(id1).style.display='none';

            	if(txt.match(id2))
            	{
            		document.getElementById(id1).style.display='table-row';
            	}
            }
		</script>
		<script>
		function convertTime() {
			console.log(moment.version);
			
			var PST = moment.tz(document.getElementById('datepicker').value, "America/Los_Angeles").format();
			var CST = moment.tz(PST, "Asia/Shanghai").format("YYYY-MM-DD ddd HH:mm");
			document.getElementById('CST').value = CST;
		}
		</script>
		<script>
		function isEmpty() {
			var courseName = document.getElementById('courseName').value;
			if(courseName == "") {
				alert("Please enter the course name");
				return false
			}
			else {
				document.courseInfo.submit();
			}
		}
		</script>
		
<title>Add a new course</title>
</head>
<body>
<div id="triton">
    <img src="css/triton2.png" class="ribbon"/>
</div>

<% User user = (User)session.getAttribute("user"); 
	String userName = user.getUserName();
%>
<div align="center">
  <h1>Add A New Course</h1>
  <form name="courseInfo" action="AddCourseController" method="post">
  <table>
   <tr>
    <td>Course Name</td>
    <td><input type="text" name="courseName" id="courseName"/></td>
   </tr>
   <tr>
    <td>Weekly Quiz</td>
    <td>
    <select name="quiz" id="quizopts" onchange="showdv(this, 'quizbox', 'Yes');" >
    <option value="">Select</option>
	<option value="Yes">Yes</option>
	<option value="No">No</option>
	</select>
    </td>
   </tr>
   <tr id="quizbox" style="display:none;">
	  <td>Quiz Details</td>
	  <td><input type="text" name="quizDetails" maxlength="100" /></td>
	  <td>Quiz Time</td>
	  <td><input type="text" name="quizDue" maxlength="50" /></td>
   </tr>
   <tr>
    <td>Weekly Homework</td>
	<td>
    <select name="homework" id="hwopts" onchange="showdv(this, 'hwbox', 'Yes');" >
    <option value="">Select</option>
	<option value="Yes">Yes</option>
	<option value="No">No</option>
	</select>
    </td>
   </tr>
   <tr id="hwbox" style="display:none;">
	  <td>Homework Details</td>
	  <td><input type="text" name="hwDetails" maxlength="100" /></td>
	  <td>HW Due Time</td>
	  <td><input type="text" name="hwDue" maxlength="50" /></td>
   </tr>
   <tr>
    <td>Reading Quiz</td>
    <td>
	<select name="readingQuiz" id="readingopts" onchange="showdv(this, 'rdbox', 'Yes');" >
	<option value="">Select</option>
    <option value="Yes">Yes</option>
    <option value="No">No</option>
    </select>
	</td>
   </tr>
   <tr id="rdbox" style="display:none;">
	  <td>RQ Details</td>
	  <td><input type="text" name="rdDetails" maxlength="100" /></td>
	  <td>RQ Due Time</td>
	  <td><input type="text" name="rdDue" maxlength="50" /></td>
   </tr>
   <tr>
    <td>Final Date</td>
    <td>
	<input type="text" name="finalDate" id="datepicker" onchange="convertTime()" readonly="readonly"/>
	</td>
	<td>CST</td>
	<td>
	<input type="text" name="cstTime" id="CST" readonly="readonly"/>
	</td>
   </tr>
  </table>
  <input type="submit" class="button" value="Add" onclick="return isEmpty()"/>
  <input type="reset" class="button" value="Reset" />
  <input type="button" class="button" value="Back" onclick="history.back()" />
  </form>
</div>
</body>
</html>