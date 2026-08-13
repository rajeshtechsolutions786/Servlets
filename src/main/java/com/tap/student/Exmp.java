package com.tap.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Exmp extends HttpServlet {
	
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
		 
		if (res.next()==true) {
			
			//Fetching Attendence details
			writer.println("<h3>Welcome " + res.getString("Full_name") +"!</h3>");
			
			String query2 = "select * from attendence";
			Statement stmt = con.createStatement();
			ResultSet res2 = stmt.executeQuery(query2);
			
			writer.println("<table border=1>\r\n"
					+ "<tr>\r\n"
					+ "<th>Id</th>\r\n"
					+ "<th>Student_id</th>\r\n"
					+ "<th>subjects</th>\r\n"
					+ "<th>Classes Attendend</th>\r\n"
					+ "<th>Total Classes</th>\r\n"
					+ "</tr>");
			
			
			while(res2.next()==true) {
				int id = res2.getInt(1);
				String student_id1 = res2.getString(2);
				String subject = res2.getString(3);
				int class_attendence = res2.getInt(4);
				int total_classes = res2.getInt(5);
				
				writer.println("<tr>\r\n"
						+ "<td>"+ id +"</td>\r\n"
						+ "<td>"+ student_id+"</td>\r\n"
						+ "<td>"+ subject +"</td>\r\n"
						+ "<td>"+ class_attendence +"</td>\r\n"
						+ "<td>"+ total_classes +"</td>\r\n"
						+ "</tr>");
			}
			
			writer.println("</table>");
			
		} else {
			RequestDispatcher rd=req.getRequestDispatcher("/invalidlogin.html");
			rd.forward(req, resp);

		}
	}catch(Exception e1) {
		e1.printStackTrace();
	}
	}
	
	
	@Override
	public void destroy() {
		try {
			res.close();
			pstmt.close();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	}


