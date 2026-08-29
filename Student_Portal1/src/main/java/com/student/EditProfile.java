package com.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/EditProfile")
public class EditProfile extends HttpServlet {
	Connection con = null;
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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter writer =resp.getWriter();
		
		int i =0;
	String Email = req.getParameter("Email");
	String Department = req.getParameter("Department");
	String Semester = req.getParameter("Semester");
	String id =req.getParameter("id");
	
	try {
		String query = "Update students set email = ?, department = ?, semester = ? where student_id = ? ";
		PreparedStatement pstmt3 = con.prepareStatement(query);
		pstmt3.setString(1, Email);
		pstmt3.setString(2, Department);
		pstmt3.setString(3, Semester);
		pstmt3.setString(4, id);
		 i = pstmt3.executeUpdate();
		 
		 
		
	} catch (Exception e1) {
		e1.printStackTrace();
	}
	
	if(i==1) {
		writer.println("<h1>Sucessfully Updated in "+id+"</h3>");
		
	}else {
		writer.println("<h1>Not Upd</h3>");
	}
}
}
