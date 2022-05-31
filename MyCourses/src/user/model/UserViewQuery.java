package user.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import courses.model.Course;

public class UserViewQuery {
	private final String SQLURL = "jdbc:mysql://localhost:3306/courses";
	private final String USERNAME = "root";
	private final String PASSWORD = "Ldf010217";
	
	private int user_id;

	public UserViewQuery(int user_id) {
		super();
		this.user_id = user_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public List<Course> listCourse() throws SQLException {
		String GET_MAP ="SELECT * FROM courses.user_course WHERE user_id = ?";
		String GET_COURSE ="SELECT * FROM courses.courses WHERE id = ?";
		
		List<Integer> courseID = new ArrayList<Integer>();
		List<Course> courseList = new ArrayList<Course>();
		
		try{
			Connection connection = connect();
			
			PreparedStatement ptmt = connection.prepareStatement(GET_MAP);
			ptmt.setInt(1, user_id);
			
			//Get IDs of all the courses to be listed from DB
			System.out.println(ptmt);
			ResultSet resultSet = ptmt.executeQuery();
			while(resultSet.next()) {
				courseID.add(resultSet.getInt("course_id"));
			}
			
			//Add all the courses requested to courseList
			ptmt = connection.prepareStatement(GET_COURSE);
			for(int id : courseID)
			{
				Course course = new Course();
				ptmt.setInt(1, id);
				System.out.println(ptmt);
				resultSet = ptmt.executeQuery();
				
				resultSet.next();
				course.setCourseName(resultSet.getString("course_name"));
				course.setFinalDate(resultSet.getString("final_date"));
				course.setHomework(resultSet.getString("weekly_hw"));
				course.setHwDue(resultSet.getString("hw_due"));
				course.setHwInfo(resultSet.getString("hw_details"));
				course.setQuiz(resultSet.getString("weekly_quiz"));
				course.setQuizInfo(resultSet.getString("quiz_details"));
				course.setQuizTime(resultSet.getString("quiz_time"));
				course.setReadingDue(resultSet.getString("reading_due"));
				course.setReadingInfo(resultSet.getString("rd_details"));
				course.setReadingQuiz(resultSet.getString("reading_quiz"));
				
				courseList.add(course);			
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return courseList;
		
	}
	
	protected Connection connect() throws SQLException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			throw new SQLException(e);
		}
		
		Connection connection = DriverManager.getConnection(SQLURL, USERNAME, PASSWORD);
		
		return connection;
		
	}
}
