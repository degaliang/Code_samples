package user.model;

import java.util.List;
import java.util.ArrayList;

public class User {
	private String userName;
	private String email;
	private String passWord;
	private int user_id;
	

	private List<String> roles; 

	public User() {
		
	}
	
	public User(String userName, String email, String passWord, List<String> roles) {
		this.userName = userName;
		this.email = email;
		this.passWord = passWord;
		this.roles = new ArrayList<String>();
		
		if(roles != null)
		{
			for(String role : roles)
			{
				this.roles.add(role);
			}
		}
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getPassWord() {
		return passWord;
	}
	
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
}
