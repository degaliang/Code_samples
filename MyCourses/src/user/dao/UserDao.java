package user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import user.model.User;

public class UserDao {
	private final boolean USER_EXIST = true;
	
	private String sqlURL = "jdbc:mysql://localhost:3306/courses";
	private String username = "root";
	private String password = "Ldf010217";
	protected Connection connection;
	
	protected void connect() throws SQLException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			throw new SQLException(e);
		}
		
		connection = DriverManager.getConnection(sqlURL, username, password);
	}

	public int addUser(User user) throws ClassNotFoundException{
		String INSERT_USERS_SQL = "INSERT INTO user" + 
				" (username, email, password) VALUES (?, ?, ?)";
		
		int result = 0;
		
		try {
			connect();
			
			PreparedStatement ptmt = connection.prepareStatement(INSERT_USERS_SQL);
			ptmt.setString(1, user.getUserName());
			ptmt.setString(2, user.getEmail());
			ptmt.setString(3, user.getPassWord());
			
			System.out.println(ptmt);
			result = ptmt.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	public int deleteUser(User user) throws ClassNotFoundException{
		
		String REMOVE_USER_SQL = "DELETE FROM user WHERE username = ?";

		int result = 0;
		
		try{
			connect();
			
			PreparedStatement ptmt = connection.prepareStatement(REMOVE_USER_SQL);
			ptmt.setString(1, user.getUserName());
			
			System.out.println(ptmt);
			result = ptmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public User findUser(User user) throws ClassNotFoundException {
		String CHECK_USER_SQL = "SELECT * from user WHERE username = ? AND password = ?";
		
		boolean result = false;
		
		try{
			connect();
			
			PreparedStatement ptmt = connection.prepareStatement(CHECK_USER_SQL);
			ptmt.setString(1, user.getUserName());
			ptmt.setString(2, user.getPassWord());
			
			System.out.println(ptmt);
			ResultSet rs = ptmt.executeQuery();
			result = rs.next();
			
			if(result == USER_EXIST)
			{
				user.setUser_id(rs.getInt("user_id"));
				user.setEmail(rs.getString("email"));
				return user; 
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
}
