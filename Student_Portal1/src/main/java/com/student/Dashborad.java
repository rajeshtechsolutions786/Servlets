package com.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Dashborad")

public class Dashborad extends HttpServlet {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet res = null;
	String url = null;
	String un = null;
	String pwd = null;
	
	@Override
	public void init(ServletConfig sc) throws ServletException {
	ServletContext 	sCon=sc.getServletContext();
	url = sCon.getInitParameter("url");
	un = sCon.getInitParameter("username");
	pwd = sCon.getInitParameter("password");
	
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection(url, un, pwd);
		
	} catch (Exception e) {
		e.printStackTrace();
		
	}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		resp.setContentType("text/html");
		PrintWriter writer =resp.getWriter();
		
		String user_id =req.getParameter("student_id");
		String pwd = req.getParameter("password");
		
		try {
			//login
			String query = "select * from students where student_id = ? and password = ?";
			 pstmt = con.prepareStatement(query);
			pstmt.setString(1, user_id);
			pstmt.setString(2, pwd);
			res = pstmt.executeQuery();
			if(res.next()== true) {
				String id = res.getString(1);
				String name = res.getString(3);
				String course = res.getString(7);
				String dept = res.getString(8);
				String sem = res.getString(9);
				
				 writer.println("<h1>Welcome " + name + "</h1>");
	                writer.println("<font color='blue'>");
	                writer.println("STUDENT ID : " + id + "&nbsp&nbsp&nbsp&nbsp&nbsp");
	                writer.println("SEMESTER      : " + sem + "<br><br><br>");
	                writer.println("COURSE		: " + course + "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
	                writer.println("DEPARTMENT	: " + dept + "<br><br><br>");
	                writer.println("=========================================================<br><br>");
	                writer.println("<a href='Profile?id=" + id + "'>Profile</a> <br>");
	                writer.println("<a href='Attendence?id=" + id + "'>Attendence</a> <br>");
	                writer.println("<a href='Finance?id=" + id + "'>Finance</a> <br>");
	                writer.println("<a href='Semester?id=" + id + "'>Semester</a> <br>");
	                writer.println("<a href='Logout'>Logout</a>");
	              
			}else {
				RequestDispatcher rd=req.getRequestDispatcher("/invalidlogin.html");
				rd.forward(req, resp);
			}
		
	}catch(Exception e1 ) {
		e1.printStackTrace();
	}
}
}



