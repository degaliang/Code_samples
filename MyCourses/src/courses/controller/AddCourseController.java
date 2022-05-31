package courses.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import courses.dao.CourseDao;
import courses.model.Course;
import user.model.User;

/**
 * Servlet implementation class AddCourseController
 */
public class AddCourseController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private CourseDao dao = new CourseDao();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCourseController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String courseName = request.getParameter("courseName");
		String quiz = request.getParameter("quiz");
		String homework = request.getParameter("homework");
		String readingQuiz = request.getParameter("readingQuiz");
		String finalDate = request.getParameter("cstTime");
		
		String quizDue = request.getParameter("quizDue");
		String quizDetails = request.getParameter("quizDetails");
		String hwDue = request.getParameter("hwDue");
		String hwDetails = request.getParameter("hwDetails");
		String rdDue = request.getParameter("rdDue");
		String rdDetails = request.getParameter("rdDetails");
		
		Course course = new Course();
		course.setCourseName(courseName);
		course.setQuiz(quiz);
		course.setHomework(homework);
		course.setReadingQuiz(readingQuiz);
		course.setFinalDate(finalDate);
		
		course.setQuizTime(quizDue);
		course.setQuizInfo(quizDetails);
		course.setHwDue(hwDue);
		course.setHwInfo(hwDetails);
		course.setReadingDue(rdDue);
		course.setReadingInfo(rdDetails);
		
		try {
			dao.addCourse(course);
			HttpSession userSession = (HttpSession) request.getSession();
			System.out.println(request.getSession());
			User user = (User)userSession.getAttribute("user");
			System.out.println(user.getUserName() + " is adding a course");
			dao.mapUser(user.getUser_id(), course);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/AddCourseComplete.jsp");
		dispatcher.forward(request, response);
	}

}
