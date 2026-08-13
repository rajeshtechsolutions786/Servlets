package com.tap.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Validation extends HttpServlet {
	

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet res = null;
	String url ="jdbc:mysql://localhost:3306/Student_portal";
	String un = "root";
    String pwd = "Prasanth kumar";
	
    @Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con =	DriverManager.getConnection(url, un, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 	
	}
    
    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		
		String student_id =req.getParameter("student_id");
		String password = req.getParameter("password");
		try {
			//login
			String query = "select * from students where student_id = ? and password = ?";
			 pstmt = con.prepareStatement(query);
			pstmt.setString(1, student_id);
			pstmt.setString(2, password);
			res = pstmt.executeQuery();
			
			if(res.next()==true) {
				writer.println("<h3>Welcome to Student portal</h3>");
				RequestDispatcher rd = req.getRequestDispatcher("/attendence");
				rd.include(req, resp);
			}else {
				RequestDispatcher rd=req.getRequestDispatcher("/invalidlogin.html");
				rd.forward(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
}
    
}