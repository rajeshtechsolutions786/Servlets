package com.student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/Welcome")
public class Welcome extends HttpServlet {

    Connection con = null;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("JDBC Load Success");

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xepdb1",
                    "system",
                    "admin"
            );

            System.out.println("Oracle Connected Successfully");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ServletException("Oracle JDBC Driver not found", e);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database connection failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");

        PrintWriter wri = res.getWriter();
        PreparedStatement pstmt =null;
        ResultSet rs =null;
        String name = req.getParameter("name");
        String pswd = req.getParameter("pswd");

        try {

            int pin = Integer.parseInt(pswd);

           

            pstmt = con.prepareStatement("SELECT * FROM student WHERE name = ? AND pin = ?");

            pstmt.setString(1, name);
            pstmt.setInt(2, pin);

            rs = pstmt.executeQuery();

            if (rs.next()) {

                int sno = rs.getInt("SNO");
                String studentName = rs.getString("NAME");
                int age = rs.getInt("AGE");
                int pinValue = rs.getInt("PIN");

         

                wri.println("<h1>Welcome " + studentName + "</h1>");
                wri.println("<font color='blue'>");
                wri.println("STUDENT ID :" + sno + "&nbsp&nbsp&nbsp&nbsp&nbsp");
                wri.println("NAME       :" + studentName + "<br><br><br>");
                wri.println("AGE		:" + age + "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
                wri.println("PIN		:" + pinValue + "<br><br><br>");
                wri.println("</font>");
                wri.println("QUICK ACCESS<br><br>");
                wri.println("<a href='Profile?name=" + studentName + "'>Profile</a>&nbsp&nbsp&nbsp&nbsp");
                wri.println("<a href='Attendence?name=" + studentName + "'>Attendence</a>&nbsp&nbsp&nbsp&nbsp");
                wri.println("<a href='Finance?name=" + studentName + "'>Finance</a><br><br>");
                wri.println("<a href='Semester?name=" + studentName + "'>Finance</a>&nbsp&nbsp&nbsp&nbsp");
                wri.println("<a href='Payment?name=" + studentName + "'>Payment</a>&nbsp&nbsp&nbsp&nbsp");
                wri.println("<a href='Logout'>Logout</a>&nbsp&nbsp&nbsp&nbsp");
            } else {
            	
                wri.println("<center><font color='red'><h3>Invalid Login</h3></font></center>");

                RequestDispatcher rd =req.getRequestDispatcher("indexLogin.html");

                rd.include(req, res);
            }

            rs.close();
            pstmt.close();

        } catch (NumberFormatException e) {

            wri.println("<h3>PIN must contain only numbers</h3>");

        } catch (SQLException e) {

            e.printStackTrace();
            wri.println("<h3>Database Error</h3>");
        }
    }

    @Override
    public void destroy() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}