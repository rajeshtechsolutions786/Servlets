package com.student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/Welcome")
public class Welcome extends HttpServlet {

    private static final long serialVersionUID = 1L;

   Connection con;

    @Override
    public void init() throws ServletException {

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xepdb1",
                    "system",
                    "admin");
            System.out.println("Database connected successfully");

        } catch (ClassNotFoundException e) {

            System.out.println("jdbc driver not connected");
            e.printStackTrace();

        } catch (SQLException e) {

            System.out.println("database not connected");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
            throws ServletException, IOException {

        PrintWriter wri=res.getWriter();
        String name=getParameter();
        wri.println("Welcome"+name);
        
    }

	private String getParameter() {
		// TODO Auto-generated method stub
		return null;
	}
}