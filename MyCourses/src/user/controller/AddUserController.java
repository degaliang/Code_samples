package user.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import user.dao.UserDao;
import user.model.User;

/**
 * Servlet implementation class AddUserController
 */
public class AddUserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UserDao usrDao = new UserDao();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUserController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("userName");
		String email = request.getParameter("email");
		String passWord = request.getParameter("passWord");
		
		User usr = new User();
		usr.setUserName(userName);
		usr.setEmail(email);
		usr.setPassWord(passWord);
		
		try {
			usrDao.addUser(usr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/RegisterComplete.jsp");
		dispatcher.forward(request, response);
	}

}
