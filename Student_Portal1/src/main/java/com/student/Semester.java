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

import com.dto.FinanceDto;
import com.dto.SemesterDto;


@WebServlet("/Semester")
public class Semester extends HttpServlet {
	
	Connection con = null;
	PreparedStatement pstmt4 = null;
	ResultSet res4 = null;
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
		double totalGpa = 0;
		double gpa = 0;
		int count = 0;
		
		resp.setContentType("text/html");
		PrintWriter writer =resp.getWriter();
		
		String id = req.getParameter("id");
		
		try {
			String query = "select * from semesters where student_id =?";
			PreparedStatement pstmt4 = con.prepareStatement(query);
			pstmt4.setString(1, id);
		 res4 = pstmt4.executeQuery();
			
		 writer.println("<h1>Semesters Page</h1>");
			writer.println("<h2>Academic Progress</h2>");
		 
			writer.println("<table border=2>\r\n"
					+ "<tr>\r\n"
					+ "<th>Sem</th>\r\n"
					+ "<th>Status</th>\r\n"
					+ "<th>GPA</th>\r\n"
					+ "</tr>");
			
			ArrayList <SemesterDto>ar3 = new ArrayList<SemesterDto>(); 
			
			while(res4.next()) {
				 int semno = res4.getInt(3);
				  String sts = res4.getString(4);
					  gpa = res4.getDouble(5);
				
					 SemesterDto  sdt = new SemesterDto();
					 sdt.setSemester(semno);
					 sdt.setStatus(sts);
					 sdt.setGpa(gpa);
					 
					 ar3.add(sdt);
					 
					 if(sts.equalsIgnoreCase("Done")) {
							totalGpa += gpa;
							count++;
					 }
					 
					 for(int i=0; i<ar3.size(); i++) {
							ar3.get(i);
						}
						
						writer.println("<tr>\r\n"
								+ "<td>"+ semno +"</td>\r\n"
								+ "<td>"+ sts +"</td>\r\n"
								+ "<td>"+ gpa +"</td>\r\n"
								+ "</tr>");
								
							}
							writer.println("</table>");
							
							writer.println("=================================== <br>");
							double cgpa = count > 0 ? (totalGpa/count) : 0;
							writer.println("Overall CGPA: " + cgpa +"<br>");
							
							writer.println("<input type='button' value='back' onclick='history.back()'>");
					        writer.println("</center>");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
