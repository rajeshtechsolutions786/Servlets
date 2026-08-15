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

@WebServlet("/Finance")
public class Finance extends HttpServlet {

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

            System.out.println("Database Connected");

        } catch (Exception e) {

            e.printStackTrace();

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
                    "SELECT * FROM finance WHERE name = ?"
            );

            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            out.println("<center>");

            out.println("<h1>Student Finance " + name + "</h1>");

            out.println("<table border='1' cellpadding='10'>");

            out.println("<tr>");

            out.println("<th>FEE TYPE</th>");
            out.println("<th>AMOUNT</th>");
            out.println("<th>STATUS</th>");

            out.println("</tr>");

            while (rs.next()) {

                String feeType = rs.getString("FEETYPE");
                int amount = rs.getInt("AMOUNT");
                String status = rs.getString("STATUS");

                out.println("<tr>");

                out.println("<td>" + feeType + "</td>");
                out.println("<td>" + amount + "</td>");
                out.println("<td>" + status + "</td>");

                out.println("</tr>");
            }

            out.println("</table><br><br>");

            out.println("<input type='button' value='back' onclick='history.back()'>");

            out.println("</center>");

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