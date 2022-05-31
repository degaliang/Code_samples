package courses.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import courses.dao.CourseDao;
import courses.model.Course;
import user.model.User;
import user.model.UserViewQuery;

/**
 * Servlet implementation class CourseController
 */
public class CourseController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private CourseDao cd = new CourseDao();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourseController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/CourseListAdd.jsp");
		dispatcher.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getServletPath();
		
		try {
			switch(action){
			case "/add":
				addCourse(request, response);
				break;
			case "/edit":
				editCourse(request, response);
				break;
			case "/view":
				viewCourse(request, response);
				break;
			case "/remove":
				removeCourse(request, response);
				break;
			default:
				break;
			}
		}catch(SQLException ex){
			throw new ServletException(ex);
		}

	}
	
	private void addCourse(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/AddCourse.jsp");
		dispatcher.forward(request, response);
	}
	
	private void editCourse(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/EditCourse.jsp");
		dispatcher.forward(request, response);
	}
	
	private void viewCourse(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ViewCourse.jsp");
		dispatcher.forward(request, response);
	}
	
	private void removeCourse(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		
		String courseName = request.getParameter("courseName");
		
		Course course = new Course();
		course.setCourseName(courseName);
		User user = (User) request.getSession().getAttribute("user");
		
		try {
			cd.removeUserMap(user.getUser_id(), course);
			cd.removeCourse(course);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/CourseListAdd.jsp");
		dispatcher.forward(request, response);
	}
}
