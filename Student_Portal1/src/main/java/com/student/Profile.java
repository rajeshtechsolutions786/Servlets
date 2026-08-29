package com.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Profile")
public class Profile extends HttpServlet {
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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html");
		PrintWriter writer =resp.getWriter();
		
		String id =req.getParameter("id");
		try {
			String query = "select * from students where student_id =?";
			PreparedStatement pstmt1 = con.prepareStatement(query);
			pstmt1.setString(1, id);
		ResultSet res1 = pstmt1.executeQuery();
		
		if(res1.next()){
			
			String name = res1.getString(3);
			String gender =res1.getString(4);
			Date dob= res1.getDate(5);
			String email = res1.getString(6);
			String course = res1.getString(7);
			String dept = res1.getString(8);
			String sem = res1.getString(9);
			Date admindate = res1.getDate(10);
			
			
			writer.println("<h1>Profile Page</h3>");
			writer.println("<h3>Personal Information</h2>");
			writer.println("Name: "+ name + "<br>");
			writer.println("Gender: "+ gender + "<br>");
			writer.println("DOB: "+ dob + "<br>");
			writer.println("Email: "+ email + "<br>");
			writer.println("------------------------------------------");
			writer.println("<h3>Academic Details</h2>");
			writer.println("Course: "+ course + "<br>");
			writer.println("Department: "+ dept + "<br>");
			writer.println("Semester: "+ sem + "<br>");
			writer.println("Admission Date: "+ admindate + "<br>");
			writer.println("-------------------------------------------- <br>");
			
			
			 writer.println("<input type='button' value='back' onclick='history.back()'>");
            // writer.println("</center>");
             
             writer.println("<input type='button' value='Edit' onclick=\"location.href='edit.html?id=" +id+ "'\">");
             writer.println("</center>");
             
           
             
             
			
		}else {
			
			writer.println("<h3>Profile Details are not found...");
			
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
