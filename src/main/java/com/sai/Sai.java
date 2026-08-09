package com.sai;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/Sai")
public class Sai extends HttpServlet {
	public void init() {
		System.out.println("Init() method");
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter wri=res.getWriter();
		wri.println("sai");
		String name=req.getParameter("name");
		String age=req.getParameter("age");
		wri.println("sucess");
		System.out.println("name : "+name);
		System.out.println("age : "+age);
		wri.println("name : "+name);
		wri.println("age : "+age);
		RequestDispatcher rd=req.getRequestDispatcher("Static.html");
		rd.forward(req, res);
	}

}
