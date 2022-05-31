package courses.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import courses.model.Course;
import user.model.User;
import user.model.UserViewQuery;

/**
 * Servlet implementation class ListUserCourse
 */
public class ListUserCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListUserCourse() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession userSession = request.getSession();
		User user = (User) userSession.getAttribute("user");
		UserViewQuery uvq = new UserViewQuery(user.getUser_id());
		
		try {
			List<Course> courseList = uvq.listCourse();
			String data = "Forwarding data: ";
			for(Course course : courseList)
			{
				data = data + course.getCourseName() + ", ";
			}
			System.out.println(data);
			HttpSession session = request.getSession();
			session.setAttribute("courseList", courseList);
			//session.setAttribute("test", "test"); This is used to test if session is forwarded
			request.setAttribute("courseSession", session);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/CourseListAdd.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
