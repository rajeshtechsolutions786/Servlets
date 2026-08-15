package com.student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/Profile")
public class Profile extends HttpServlet {

    Connection con;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");

            con = java.sql.DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xepdb1",
                    "system",
                    "admin"
            );

            System.out.println("Profile Database Connected");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Database Connection Failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");

        PrintWriter out = res.getWriter();

        String name = req.getParameter("name");

        try {

            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT * FROM student WHERE name = ?"
            );

            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
            	
                int sno = rs.getInt("SNO");
                String studentName = rs.getString("NAME");
                int age = rs.getInt("AGE");
                int pin = rs.getInt("PIN");

                out.println("<center>");

                out.println("<h1>Student Profile</h1>");

                out.println("<table border='1' cellpadding='10'>");

                out.println("<tr>");
                out.println("<th>SNO</th>");
                out.println("<th>NAME</th>");
                out.println("<th>AGE</th>");
                out.println("<th>PIN</th>");
                out.println("</tr>");

                out.println("<tr>");
                out.println("<td>" + sno + "</td>");
                out.println("<td>" + studentName + "</td>");
                out.println("<td>" + age + "</td>");
                out.println("<td>" + pin + "</td>");
                out.println("</tr>");

                out.println("</table><br><br>");
                out.println("<input type='button' value='back' onclick='history.back()'>");
                out.println("</center>");
              
            } else {

                out.println("<h3>Profile Not Found</h3>");

            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {

            e.printStackTrace();
            out.println("<h3>Database Error</h3>");

        }
    }

    @Override
    public void destroy() {

        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}