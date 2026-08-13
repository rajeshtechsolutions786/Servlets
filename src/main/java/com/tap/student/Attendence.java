package com.tap.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Attendence extends HttpServlet {
	

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
		
		PrintWriter writer = resp.getWriter();
		try {
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
					+ "<td>"+ student_id1+"</td>\r\n"
					+ "<td>"+ subject +"</td>\r\n"
					+ "<td>"+ class_attendence +"</td>\r\n"
					+ "<td>"+ total_classes +"</td>\r\n"
					+ "</tr>");
		}
		
		writer.println("</table>");
		
		req.getRequestDispatcher("/eligible").include(req, resp);
		
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
}
