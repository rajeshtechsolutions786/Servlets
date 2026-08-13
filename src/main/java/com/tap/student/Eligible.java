package com.tap.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Eligible extends HttpServlet {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet res1 = null;
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
    	
    	String student_id = req.getParameter("student_id");
    	String password = req.getParameter("password");
    	
    	PrintWriter writer = resp.getWriter();
    	try {
    	String query = "select * from Attendence where student_id = ? ";
		 pstmt = con.prepareStatement(query);
		pstmt.setString(1, student_id);
		res1 = pstmt.executeQuery();
		res1.next();
			
		String subject = res1.getString(3);
		int classes = res1.getInt(4);
		
		//String query2 = "select * from Attendence where "
		writer.println("<h3>"+res1.getString(2)+" "
				+"attendence is eligiable for</h3>");

		writer.println("<table border=1>\r\n"
				+ "<tr>\r\n"
				+ "<th>Id</th>\r\n"
				+ "<th>Student_id</th>\r\n"
				+ "<th>subjects</th>\r\n"
				+ "<th>Classes Attendend</th>\r\n"
				+ "<th>Total Classes</th>\r\n"
				+ "</tr>");
		
		
		while(res1.next()==true) {
			int id = res1.getInt(1);
			String student_id1 = res1.getString(2);
			String subjects = res1.getString(3);
			int class_attendence1 = res1.getInt(4);
			int total_classes = res1.getInt(5);
			
			writer.println("<tr>\r\n"
					+ "<td>"+ id +"</td>\r\n"
					+ "<td>"+ student_id1+"</td>\r\n"
					+ "<td>"+ subjects+"</td>\r\n"
					+ "<td>"+ class_attendence1 +"</td>\r\n"
					+ "<td>"+ total_classes +"</td>\r\n"
					+ "</tr>");
		}
		
		writer.println("</table>");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
}
