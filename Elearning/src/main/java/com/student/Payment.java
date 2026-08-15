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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

@WebServlet("/Payment")
public class Payment extends HttpServlet {

    Connection con;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/xepdb1",
                    "system",
                    "admin"
            );
            System.out.println("Payment Database Connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String action = req.getParameter("action");
        
        if ("view".equals(action)) {
            viewAllPayments(out);
            return;
        }

        // Display payment page with QR code
        displayPaymentPage(out);
    }

    private void displayPaymentPage(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>PhonePe QR Payment</title>");
        out.println("<style>");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body {");
        out.println("    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
        out.println("    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("    min-height: 100vh;");
        out.println("    display: flex;");
        out.println("    justify-content: center;");
        out.println("    align-items: center;");
        out.println("    padding: 20px;");
        out.println("}");
        out.println(".container {");
        out.println("    max-width: 550px;");
        out.println("    background: white;");
        out.println("    padding: 40px;");
        out.println("    border-radius: 20px;");
        out.println("    box-shadow: 0 15px 40px rgba(0,0,0,0.3);");
        out.println("    text-align: center;");
        out.println("}");
        out.println(".header h1 { color: #4a5568; font-size: 28px; }");
        out.println(".header .phonepe { color: #667eea; font-weight: bold; }");
        out.println(".qr-container {");
        out.println("    background: white;");
        out.println("    padding: 20px;");
        out.println("    border-radius: 15px;");
        out.println("    border: 3px dashed #667eea;");
        out.println("    margin: 20px 0;");
        out.println("    display: inline-block;");
        out.println("}");
        out.println(".qr-container img { max-width: 250px; height: auto; }");
        out.println(".upi-details {");
        out.println("    background: #f7fafc;");
        out.println("    padding: 15px;");
        out.println("    border-radius: 10px;");
        out.println("    margin: 15px 0;");
        out.println("    border-left: 4px solid #667eea;");
        out.println("}");
        out.println(".upi-details p { margin: 8px 0; color: #4a5568; }");
        out.println(".highlight { color: #667eea; font-weight: bold; font-size: 18px; }");
        out.println(".form-group { margin-bottom: 15px; text-align: left; }");
        out.println(".form-group label {");
        out.println("    display: block;");
        out.println("    margin-bottom: 5px;");
        out.println("    color: #4a5568;");
        out.println("    font-weight: 600;");
        out.println("}");
        out.println(".form-group input {");
        out.println("    width: 100%;");
        out.println("    padding: 12px;");
        out.println("    border: 2px solid #e2e8f0;");
        out.println("    border-radius: 8px;");
        out.println("    font-size: 16px;");
        out.println("}");
        out.println(".form-group input:focus {");
        out.println("    border-color: #667eea;");
        out.println("    outline: none;");
        out.println("}");
        out.println(".btn-submit {");
        out.println("    width: 100%;");
        out.println("    padding: 14px;");
        out.println("    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("    color: white;");
        out.println("    border: none;");
        out.println("    border-radius: 10px;");
        out.println("    font-size: 18px;");
        out.println("    font-weight: bold;");
        out.println("    cursor: pointer;");
        out.println("    transition: all 0.3s;");
        out.println("}");
        out.println(".btn-submit:hover { transform: scale(1.02); }");
        out.println(".btn-view {");
        out.println("    display: inline-block;");
        out.println("    padding: 10px 20px;");
        out.println("    background: #28a745;");
        out.println("    color: white;");
        out.println("    text-decoration: none;");
        out.println("    border-radius: 8px;");
        out.println("    margin-top: 15px;");
        out.println("    font-weight: bold;");
        out.println("}");
        out.println(".instruction {");
        out.println("    background: #fff3cd;");
        out.println("    padding: 12px;");
        out.println("    border-radius: 8px;");
        out.println("    margin: 15px 0;");
        out.println("    border-left: 4px solid #ffc107;");
        out.println("    text-align: left;");
        out.println("}");
        out.println(".instruction p { margin: 5px 0; color: #856404; }");
        out.println(".footer { margin-top: 20px; color: #718096; font-size: 14px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        
        out.println("<div class='header'>");
        out.println("<h1>📱 <span class='phonepe'>PhonePe</span> Payment</h1>");
        out.println("</div>");
        
        // QR Code Section
        out.println("<div class='qr-container'>");
        out.println("<img src='https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=upi://pay?pa=9502638415-3@ybl&pn=PhonePe%20Payment&am=1.00&cu=INR' alt='PhonePe QR Code'>");
        out.println("</div>");
        
        out.println("<div class='upi-details'>");
        out.println("<p>💳 <strong>Scan QR to Pay ₹1</strong></p>");
        out.println("<p>UPI ID: <span class='highlight'>9502638415-3@ybl</span></p>");
        out.println("<p>Amount: <span class='highlight'>₹1.00</span></p>");
        out.println("</div>");
        
        out.println("<div class='instruction'>");
        out.println("<p>📌 <strong>Payment Steps:</strong></p>");
        out.println("<p>1. Scan QR code using PhonePe/Google Pay</p>");
        out.println("<p>2. Pay exactly ₹1.00</p>");
        out.println("<p>3. Enter your name and phone below</p>");
        out.println("<p>4. Click 'Verify Payment' to confirm</p>");
        out.println("</div>");
        
        // Payment verification form
        out.println("<form action='Payment' method='POST'>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='name'>👤 Full Name</label>");
        out.println("<input type='text' id='name' name='name' placeholder='Enter your name (sai, sasi, or rishi)' required>");
        out.println("</div>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='phone'>📞 Phone Number</label>");
        out.println("<input type='tel' id='phone' name='phone' placeholder='Enter 10-digit phone number' pattern='[0-9]{10}' maxlength='10' required>");
        out.println("</div>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='upiId'>🔑 UPI ID (Paid From)</label>");
        out.println("<input type='text' id='upiId' name='upiId' placeholder='Enter your UPI ID (e.g., example@paytm)' value='9502638415-3@ybl' required>");
        out.println("</div>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='amount'>💰 Amount (₹)</label>");
        out.println("<input type='text' id='amount' name='amount' value='1.00' readonly style='background:#f7fafc;'>");
        out.println("</div>");
        
        out.println("<button type='submit' class='btn-submit'>✅ Verify Payment</button>");
        out.println("</form>");
        
        out.println("<a href='Payment?action=view' class='btn-view'>📊 View All Payments</a>");
        
        out.println("<div class='footer'>");
        out.println("<p>🔒 Secure Payment via PhonePe UPI</p>");
        out.println("</div>");
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String upiId = req.getParameter("upiId");
        String amount = req.getParameter("amount");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Payment Status</title>");
        out.println("<style>");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body {");
        out.println("    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
        out.println("    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("    min-height: 100vh;");
        out.println("    display: flex;");
        out.println("    justify-content: center;");
        out.println("    align-items: center;");
        out.println("    padding: 20px;");
        out.println("}");
        out.println(".container {");
        out.println("    max-width: 500px;");
        out.println("    background: white;");
        out.println("    padding: 40px;");
        out.println("    border-radius: 20px;");
        out.println("    box-shadow: 0 15px 40px rgba(0,0,0,0.3);");
        out.println("    text-align: center;");
        out.println("}");
        out.println(".success { color: #28a745; font-size: 32px; }");
        out.println(".error { color: #dc3545; font-size: 32px; }");
        out.println(".emoji-big { font-size: 60px; margin: 15px 0; }");
        out.println(".details {");
        out.println("    background: #f7fafc;");
        out.println("    padding: 20px;");
        out.println("    border-radius: 10px;");
        out.println("    margin: 20px 0;");
        out.println("    text-align: left;");
        out.println("}");
        out.println(".details p { margin: 10px 0; color: #4a5568; }");
        out.println(".btn-group {");
        out.println("    display: flex;");
        out.println("    gap: 10px;");
        out.println("    justify-content: center;");
        out.println("    flex-wrap: wrap;");
        out.println("}");
        out.println(".btn {");
        out.println("    display: inline-block;");
        out.println("    padding: 12px 25px;");
        out.println("    border-radius: 8px;");
        out.println("    text-decoration: none;");
        out.println("    font-weight: bold;");
        out.println("    transition: all 0.3s;");
        out.println("}");
        out.println(".btn-primary {");
        out.println("    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("    color: white;");
        out.println("}");
        out.println(".btn-success { background: #28a745; color: white; }");
        out.println(".status-badge {");
        out.println("    display: inline-block;");
        out.println("    padding: 8px 20px;");
        out.println("    border-radius: 20px;");
        out.println("    font-weight: bold;");
        out.println("}");
        out.println(".badge-paid { background: #d4edda; color: #155724; }");
        out.println(".badge-notpaid { background: #f8d7da; color: #721c24; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        try {
            // Check if user exists
            String checkQuery = "SELECT * FROM payment WHERE name = ? AND num = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setString(1, name);
            checkStmt.setString(2, phone);
            
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // User exists
                String currentStatus = rs.getString("payment");
                
                // Check if already paid
                if ("paid".equalsIgnoreCase(currentStatus)) {
                    out.println("<div class='emoji-big'>🎉</div>");
                    out.println("<h1 class='success'>Already Paid!</h1>");
                    out.println("<p style='font-size: 18px;'>" + name + " has already paid ₹1</p>");
                    out.println("<div class='details'>");
                    out.println("<p><strong>📱 Phone:</strong> " + phone + "</p>");
                    out.println("<p><strong>✅ Status:</strong> <span class='status-badge badge-paid'>PAID ✓</span></p>");
                    out.println("</div>");
                    out.println("<div class='btn-group'>");
                    out.println("<a href='Payment' class='btn btn-primary'>🔄 Back</a>");
                    out.println("<a href='Payment?action=view' class='btn btn-success'>📊 View All</a>");
                    out.println("</div>");
                    out.println("</div></body></html>");
                    return;
                }
                
                // Validate amount - must be 1.00
                if (!"1.00".equals(amount) && !"1".equals(amount) && !"1.0".equals(amount)) {
                    out.println("<div class='emoji-big'>⚠️</div>");
                    out.println("<h1 class='error'>Invalid Amount</h1>");
                    out.println("<p>Please pay exactly <strong>₹1.00</strong></p>");
                    out.println("<p>You entered: ₹" + amount + "</p>");
                    out.println("<div class='btn-group'>");
                    out.println("<a href='Payment' class='btn btn-primary'>🔄 Try Again</a>");
                    out.println("</div>");
                    out.println("</div></body></html>");
                    return;
                }
                
                // Update payment status to 'paid'
                String updateQuery = "UPDATE payment SET payment = 'paid' WHERE name = ? AND num = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setString(1, name);
                updateStmt.setString(2, phone);
                
                int rowsAffected = updateStmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Payment successful
                    out.println("<div class='emoji-big'>🎉</div>");
                    out.println("<h1 class='success'>✅ Payment Successful!</h1>");
                    out.println("<p style='font-size: 18px; margin: 10px 0;'>Thank you <strong>" + name + "</strong></p>");
                    out.println("<p style='color: #28a745; font-size: 16px;'>Your payment of ₹1 has been confirmed</p>");
                    
                    out.println("<div class='details'>");
                    out.println("<p><strong>📱 Phone:</strong> " + phone + "</p>");
                    out.println("<p><strong>🔑 UPI ID:</strong> " + upiId + "</p>");
                    out.println("<p><strong>💰 Amount:</strong> ₹" + amount + "</p>");
                    out.println("<p><strong>✅ Status:</strong> <span class='status-badge badge-paid'>PAID ✓</span></p>");
                    out.println("</div>");
                    
                    out.println("<div style='margin: 15px 0;'>");
                    out.println("<div style='display: inline-block; background: #d4edda; padding: 10px 20px; border-radius: 50px;'>");
                    out.println("💰 Payment Confirmed!");
                    out.println("</div>");
                    out.println("</div>");
                    
                } else {
                    out.println("<div class='emoji-big'>❌</div>");
                    out.println("<h1 class='error'>Payment Failed</h1>");
                    out.println("<p>Unable to update payment status. Please try again.</p>");
                }
                updateStmt.close();
                
            } else {
                // User not found
                out.println("<div class='emoji-big'>❌</div>");
                out.println("<h1 class='error'>User Not Found</h1>");
                out.println("<p>No user found with name: <strong>" + name + "</strong> and phone: <strong>" + phone + "</strong></p>");
                
                out.println("<div class='details'>");
                out.println("<p><strong>📱 Phone:</strong> " + phone + "</p>");
                out.println("<p><strong>🔑 UPI ID:</strong> " + upiId + "</p>");
                out.println("<p><strong>💰 Amount:</strong> ₹" + amount + "</p>");
                out.println("</div>");
                
                out.println("<div style='background: #fff3cd; padding: 15px; border-radius: 8px; margin: 15px 0; border-left: 4px solid #ffc107;'>");
                out.println("<p style='color: #856404;'>💡 <strong>Available Users:</strong></p>");
                out.println("<p style='color: #856404;'>• sai - 9502638415</p>");
                out.println("<p style='color: #856404;'>• sasi - 9618350961</p>");
                out.println("<p style='color: #856404;'>• rishi - 8328348700</p>");
                out.println("</div>");
            }
            
            rs.close();
            checkStmt.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<div class='emoji-big'>💻</div>");
            out.println("<h1 class='error'>Database Error</h1>");
            out.println("<p style='color:#dc3545;'>Error: " + e.getMessage() + "</p>");
            out.println("<p style='color:#856404;'>Please check your database connection and table structure.</p>");
        }

        out.println("<div class='btn-group'>");
        out.println("<a href='Payment' class='btn btn-primary'>🔄 Try Again</a>");
        out.println("<a href='Payment?action=view' class='btn btn-success'>📊 View All</a>");
        out.println("</div>");
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    private void viewAllPayments(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>All Payments</title>");
        out.println("<style>");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body {");
        out.println("    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
        out.println("    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("    min-height: 100vh;");
        out.println("    display: flex;");
        out.println("    justify-content: center;");
        out.println("    align-items: center;");
        out.println("    padding: 20px;");
        out.println("}");
        out.println(".container {");
        out.println("    max-width: 800px;");
        out.println("    background: white;");
        out.println("    padding: 30px;");
        out.println("    border-radius: 20px;");
        out.println("    box-shadow: 0 15px 40px rgba(0,0,0,0.3);");
        out.println("}");
        out.println("h1 { color: #4a5568; text-align: center; margin-bottom: 20px; }");
        out.println(".summary {");
        out.println("    display: flex;");
        out.println("    justify-content: space-around;");
        out.println("    margin: 20px 0;");
        out.println("    flex-wrap: wrap;");
        out.println("}");
        out.println(".summary-box {");
        out.println("    background: #f7fafc;");
        out.println("    padding: 15px 25px;");
        out.println("    border-radius: 10px;");
        out.println("    text-align: center;");
        out.println("    min-width: 120px;");
        out.println("}");
        out.println(".summary-box .number { font-size: 24px; font-weight: bold; }");
        out.println(".summary-box .label { color: #718096; font-size: 14px; }");
        out.println(".summary-box .paid-color { color: #28a745; }");
        out.println(".summary-box .pending-color { color: #dc3545; }");
        out.println("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        out.println("th {");
        out.println("    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("    color: white;");
        out.println("    padding: 12px;");
        out.println("    text-align: left;");
        out.println("}");
        out.println("td { padding: 12px; border-bottom: 1px solid #e2e8f0; }");
        out.println("tr:hover { background: #f7fafc; }");
        out.println(".paid { color: #28a745; font-weight: bold; }");
        out.println(".not-paid { color: #dc3545; font-weight: bold; }");
        out.println(".btn {");
        out.println("    display: inline-block;");
        out.println("    padding: 10px 20px;");
        out.println("    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        out.println("    color: white;");
        out.println("    text-decoration: none;");
        out.println("    border-radius: 8px;");
        out.println("    font-weight: bold;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>📊 Payment Status</h1>");

        try {
            // Get statistics using correct column names
            PreparedStatement countStmt = con.prepareStatement(
                "SELECT COUNT(*) as total, " +
                "SUM(CASE WHEN payment = 'paid' THEN 1 ELSE 0 END) as paid_count " +
                "FROM payment"
            );
            ResultSet countRs = countStmt.executeQuery();
            
            int total = 0;
            int paid = 0;
            if (countRs.next()) {
                total = countRs.getInt("total");
                paid = countRs.getInt("paid_count");
            }
            countRs.close();
            countStmt.close();
            
            int pending = total - paid;
            
            out.println("<div class='summary'>");
            out.println("<div class='summary-box'>");
            out.println("<div class='number'>" + total + "</div>");
            out.println("<div class='label'>Total Users</div>");
            out.println("</div>");
            out.println("<div class='summary-box'>");
            out.println("<div class='number paid-color'>" + paid + "</div>");
            out.println("<div class='label'>✅ Paid</div>");
            out.println("</div>");
            out.println("<div class='summary-box'>");
            out.println("<div class='number pending-color'>" + pending + "</div>");
            out.println("<div class='label'>⏳ Pending</div>");
            out.println("</div>");
            out.println("</div>");

            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT * FROM payment ORDER BY id"
            );
            ResultSet rs = pstmt.executeQuery();

            out.println("<table>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>NAME</th>");
            out.println("<th>PHONE</th>");
            out.println("<th>STATUS</th>");
            out.println("</tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String num = rs.getString("num");
                String status = rs.getString("payment");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + num + "</td>");
                
                if ("paid".equalsIgnoreCase(status)) {
                    out.println("<td class='paid'>✅ PAID</td>");
                } else {
                    out.println("<td class='not-paid'>❌ NOT PAID</td>");
                }
                
                out.println("</tr>");
            }

            out.println("</table>");
            rs.close();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p style='color:#dc3545;'>Database Error: " + e.getMessage() + "</p>");
            out.println("<p style='color:#856404;'>Please run the SQL commands to create the table properly.</p>");
        }

        out.println("<br><center><a href='Payment' class='btn'>🔙 Back to Payment</a></center>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    public void destroy() {
        try {
            if (con != null) {
                con.close();
                System.out.println("Payment Database Disconnected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}