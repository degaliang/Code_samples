package courses.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import courses.model.Course;

public class CourseDao {
	private String sqlURL = "jdbc:mysql://localhost:3306/courses";
	private String username = "root";
	private String password = "Ldf010217";
	Connection connection;
	
	protected void connect() throws SQLException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			throw new SQLException(e);
		}
		
		connection = DriverManager.getConnection(sqlURL, username, password);
	}
	
	public int addCourse(Course course) throws ClassNotFoundException{
	
		String INSERT_USERS_SQL = "INSERT INTO courses" + 
			" (course_name, weekly_quiz, quiz_time, quiz_details, weekly_hw, "
			+ "hw_due, hw_details, reading_quiz, reading_due, rd_details, final_date) VALUES" +
			" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
		int result = 0;
		
		try{
			connect();
			
			PreparedStatement ptmt = connection.prepareStatement(INSERT_USERS_SQL);
			ptmt.setString(1, course.getCourseName());
			ptmt.setString(2, course.getQuiz());
			ptmt.setString(3, course.getQuizTime());
			ptmt.setString(4, course.getQuizInfo());
			ptmt.setString(5, course.getHomework());
			ptmt.setString(6, course.getHwDue());
			ptmt.setString(7, course.getHwInfo());
			ptmt.setString(8, course.getReadingQuiz());
			ptmt.setString(9, course.getReadingDue());
			ptmt.setString(10, course.getReadingInfo());
			ptmt.setString(11, course.getFinalDate());
			
			System.out.println(ptmt);
			result = ptmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	
	}
	
	public int removeCourse(Course course) throws ClassNotFoundException{
		String REMOVE_COURSE_SQL = "DELETE FROM courses WHERE course_name=?";
		
		int result = 0;
		
		try{
			connect();
			
			PreparedStatement ptmt = connection.prepareStatement(REMOVE_COURSE_SQL);
			ptmt.setString(1, course.getCourseName());
			
			System.out.println(ptmt);
			result = ptmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int editCourse(Course course) throws ClassNotFoundException{
		String EDIT_COURSE_SQL = "UPDATE courses SET weekly_quiz = ?, weekly_hw = ?,"
				+ " reading_quiz = ?, final_date = ? , quiz_time = ?, quiz_details = ?, "
				+ "hw_due = ?, hw_details = ?, reading_due = ?, rd_details = ? WHERE course_name = ?";
		
		int result = 0;
		
		try{
			connect();
			
			PreparedStatement ptmt = connection.prepareStatement(EDIT_COURSE_SQL);
			ptmt.setString(1, course.getQuiz());
			ptmt.setString(2, course.getHomework());
			ptmt.setString(3, course.getReadingQuiz());
			ptmt.setString(4, course.getFinalDate());
			
			ptmt.setString(5, course.getQuizTime());
			ptmt.setString(6, course.getQuizInfo());
			ptmt.setString(7, course.getHwDue());
			ptmt.setString(8, course.getHwInfo());
			ptmt.setString(9, course.getReadingDue());
			ptmt.setString(10, course.getReadingInfo());
			
			ptmt.setString(11, course.getCourseName());
			
			System.out.println(ptmt);
			result = ptmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int mapUser(int user_id, Course course) throws ClassNotFoundException{
		String GET_ID = "SELECT * FROM courses.courses WHERE course_name=?";
		String MAP_USER_COURSE = "INSERT INTO courses.user_course (user_id, course_id) VALUES (?, ?);";
		
		ResultSet resultSet = null;
		int result = 0;
		int course_id = 0;
		
		try{
			connect();
			
			PreparedStatement ptmt = connection.prepareStatement(GET_ID);
			ptmt.setString(1, course.getCourseName());
			
			System.out.println(ptmt);
			resultSet = ptmt.executeQuery();
			while(resultSet.next()) {
				course_id = resultSet.getInt("id");
			}
			
			ptmt = connection.prepareStatement(MAP_USER_COURSE);
			ptmt.setInt(1, user_id);
			ptmt.setInt(2, course_id);
			System.out.println(ptmt);
			result = ptmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	public int removeUserMap(int user_id, Course course) throws ClassNotFoundException{
		String GET_COURSE_ID = "SELECT * FROM courses.courses WHERE course_name=?";
		String GET_MAP_ID = "SELECT * FROM courses.user_course WHERE course_id=?";
		String REMOVE_USER_MAP = "DELETE FROM user_course WHERE (user_id, course_id, id) IN ((?,?,?));";
		
		ResultSet resultSet = null;
		int result = 0;
		int course_id = 0;
		int id = 0;
		
		try{
			connect();
			
			//Get course_id
			PreparedStatement ptmt = connection.prepareStatement(GET_COURSE_ID);
			ptmt.setString(1, course.getCourseName());
			System.out.println(ptmt);
			resultSet = ptmt.executeQuery();
			while(resultSet.next()) {
				course_id = resultSet.getInt("id");
			}
			
			//Get map id
			ptmt = connection.prepareStatement(GET_MAP_ID);
			ptmt.setInt(1, course_id);
			System.out.println(ptmt);
			resultSet = ptmt.executeQuery();
			while(resultSet.next()) {
				id = resultSet.getInt("id");
			}
			
			//Remove user_course map
			ptmt = connection.prepareStatement(REMOVE_USER_MAP);
			ptmt.setInt(1, user_id);
			ptmt.setInt(2, course_id);
			ptmt.setInt(3, id);
			System.out.println(ptmt);
			result = ptmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
}
