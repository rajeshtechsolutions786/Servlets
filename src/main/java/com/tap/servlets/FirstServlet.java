package com.tap.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FirstServlet extends HttpServlet{
//	
//	@Override
//	public void init() throws ServletException {
//		System.out.println("init() method Called");
//	
	//}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
  
//		String skills = req.getParameter("techSkills");
//		
//		System.out.println("Name = "+ name);
//		System.out.println("Designation = "+ desig);
//		System.out.println("Tech Skills  = "+ skills);
//		
//		Enumeration<String> pn =req.getParameterNames();
//		while(pn.hasMoreElements()) s
//			System.out.println(pn.nextElement());
//		}
		 String name =req.getParameter("name");
		 String desig = req.getParameter("desig");
		 String[] pv =req.getParameterValues("techSkills");
		
		 System.out.println("Name = "+ name);
		System.out.println("Designation = "+ desig);
		System.out.println("TechSkills:");
		for(String i: pv) {
			System.out.println(i);
			 String techSkills  = req.getParameter("pv");	
		}
		
		//send response to client
		resp.setContentType("text/html");
		PrintWriter writer =resp.getWriter();
		writer.println("<html>\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"UTF-8\">\r\n"
				+ "<title>Insert title here</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "<h3> Thank you for your Response "+ name+ "</h3>\r\n"
				+ "</body>\r\n"
				+ "</html>");	
		
//		RequestDispatcher rd =req.getRequestDispatcher("staticresp.html");
//		rd.forward(req, resp);
//		
	
	/*}
	
	@Override
	public void destroy() {
		System.out.println("destroy() method Called");
		
	}*/
}
}