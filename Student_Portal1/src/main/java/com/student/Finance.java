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

@WebServlet("/Finance")
public class Finance extends HttpServlet {
	
	Connection con = null;
	PreparedStatement pstmt3 = null;
	ResultSet res3 = null;
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
		
		double amount =0;
		double amount1 = 0;
		
		resp.setContentType("text/html");
		PrintWriter writer =resp.getWriter();
		
		String id =req.getParameter("id");
		
		try {
			String query = "select * from finance where student_id =?";
			PreparedStatement pstmt3 = con.prepareStatement(query);
			pstmt3.setString(1, id);
		 res3 = pstmt3.executeQuery();
		 double totalPaid = 0;
		 double totalPending = 0;
		 
		 writer.println("<h1>Finance Page</h1>");
			writer.println("<h2>Fee Status</h2>");
			
			
			writer.println("<table border=2>\r\n"
					+ "<tr>\r\n"
					+ "<th>Fee Type</th>\r\n"
					+ "<th>Amount</th>\r\n"
					+ "<th>Status</th>\r\n"
					+ "</tr>");
			ArrayList<FinanceDto> ar2 = new ArrayList<FinanceDto>();
			
			while (res3.next()) {
			String type = res3.getString(3);
			Double amt = res3.getDouble(4);
		String sts = res3.getString(5);
		
		FinanceDto fdt = new FinanceDto();
		fdt.setFee(type);
		fdt.setAmount(amt);
		fdt.setStatus(sts);
		
		ar2.add(fdt);
		if(sts.equalsIgnoreCase("paid"))  {
			amount =totalPaid += amt;
			
		}else {
			amount1 =totalPending +=amt;
			
		}
		
		for(int i=0; i<ar2.size(); i++) {
			ar2.get(i);
		}
		
		writer.println("<tr>\r\n"
				+ "<td>"+ type +"</td>\r\n"
				+ "<td>"+ amt +"</td>\r\n"
				+ "<td>"+ sts +"</td>\r\n"
				+ "</tr>");
				
			}
			writer.println("</table>");
			
			writer.println("=================================================== <br>");
			writer.println("Total Paid: "+ amount +"<br>" );
			writer.println("Pending: "+ amount1 +"<br>");
			
			writer.println("<input type='button' value='back' onclick='history.back()'>");
	        writer.println("</center>");
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
