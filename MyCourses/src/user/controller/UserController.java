package user.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import user.dao.UserDao;
import user.model.User;

/**
 * Servlet implementation class UserController
 */
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/LoginView.jsp");

		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getServletPath();
		
		System.out.println("Executing action: " + action);
		
		try {
			switch(action){
			case "/register":
				addUser(request, response);
				break;
			case "/login":
				login(request, response);
				break;
			default:
				break;
			}
		} catch(SQLException ex){
			throw new ServletException(ex);
		}
		
	}
	
	private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Register.jsp");
		dispatcher.forward(request, response);
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

		UserDao usrDao = new UserDao();
		
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");
		
		System.out.println("Username:" + userName + " Password:" + passWord + " Logging in");
		
		User usr = new User();
		usr.setUserName(userName);
		usr.setPassWord(passWord);
		
		try {
			User user = usrDao.findUser(usr);
			if(user != null)
			{
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				System.out.println("Current user's id is " + ((User) session.getAttribute("user")).getUser_id() + "  Session is created successfully");
				request.setAttribute("userSession", session);
				System.out.println("Current session is " + request.getAttribute("userSession"));
				//RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/CourseListAdd.jsp");
				//dispatcher.forward(request, response);
				response.sendRedirect(request.getContextPath() + "/CourseController");
			}
			else
			{
				request.setAttribute("err_msg", "Invalid username or password, please try again");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/LoginView.jsp");
				dispatcher.forward(request, response);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
