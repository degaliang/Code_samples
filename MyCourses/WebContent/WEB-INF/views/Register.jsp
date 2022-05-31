<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Register</title>
</head>
<body>
	<div align="center">
		<h1>Create A New Account</h1>
		<form action="AddUserController" method="post">
			<table>
				<tr>
					<td>Enter your username: </td>
					<td><input type="text" name="userName" /></td>
				</tr>
				<tr>
					<td>Enter your email: </td>
					<td><input type="text" name="email" /></td>
				</tr>
				<tr>
					<td>Enter your password: </td>
					<td><input type="text" name="passWord" /></td>
				</tr>
			</table>
			<input type="submit" class="button" value="Register" />
		</form>
	</div>
</body>
</html>