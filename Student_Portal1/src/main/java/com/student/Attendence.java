package com.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dto.AttendenceDto;


@WebServlet("/Attendence")
public class Attendence extends HttpServlet {
	
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet res2 = null;
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
		
		int class_attendence= 0;
		int total_classes = 0;
		
		resp.setContentType("text/html");
		PrintWriter writer =resp.getWriter();
		
		String id =req.getParameter("id");
		
		try {
			String query = "select * from attendence where student_id =?";
			PreparedStatement pstmt2 = con.prepareStatement(query);
			pstmt2.setString(1, id);
		 res2 = pstmt2.executeQuery();
		
		 int totalAttendend = 0;
		int totalClasses = 0;
		
		writer.println("<h1>Attendence Page</h1>");
		writer.println("<h2>Subject Attendence</h2>");
		
		
		writer.println("<table border=2>\r\n"
				+ "<tr>\r\n"
				+ "<th>subjects</th>\r\n"
				+ "<th>Classes Attendend</th>\r\n"
				+ "<th>Total Classes</th>\r\n"
				+ "</tr>");
		ArrayList <AttendenceDto>ar = new ArrayList<AttendenceDto>();
		
		while(res2.next()) {
			//String studentId= res2.getString(2);
			String sub = res2.getString(3);
			class_attendence = res2.getInt(4);
			 total_classes = res2.getInt(5);
			//double percentage = ((double)class_attendence/total_classes)*100;
			
			writer.println("<tr>\r\n"
					+ "<td>"+ sub +"</td>\r\n"
					+ "<td>"+ class_attendence +"</td>\r\n"
					+ "<td>"+ total_classes +"</td>\r\n"
					+ "</tr>");
			
			AttendenceDto adt = new AttendenceDto();
			adt.setSubject(sub);
			adt.setPresent(class_attendence);
			adt.setTotal(total_classes);
			
			ar.add(adt);
			
			totalAttendend += class_attendence;
			totalClasses += total_classes;
			
			for(int i=0; i<ar.size(); i++) {
				ar.get(i);
			}
			
			
		}
		writer.println("</table>");
		writer.println("-----------------------------------------------<br>");
		
		writer.println("Total Attended: "+ totalAttendend +"<br>" );
		writer.println("Total Classes: "+ totalClasses +"<br>");
		writer.println("-----------------------------------------------<br>");
		writer.println("<h3>Overall Percentage</h3>");
		double overallPercentage = ((double)totalAttendend/totalClasses)*100;
		writer.println("Overall: " + overallPercentage +"<br>");
		if(overallPercentage>=75) {
			writer.println("Status: Good (Requried met) <br>");
		}else {
			writer.println("Low Attendence <br>");
		}
		
		writer.println("<input type='button' value='back' onclick='history.back()'>");
        writer.println("</center>");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
