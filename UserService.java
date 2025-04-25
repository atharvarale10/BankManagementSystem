package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    // Login using emailId only
    public boolean login(String email) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM User_Profile WHERE emailId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Register using only emailId — insert with default/empty values
    public void registerUser(String email, String name, String phone, double hotcash, double budget, int notificationNo, int incomeNo, int accountNo, int expenseNo, String friendId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO User_Profile (emailId, name, phoneNumber, hotcash, budget, notificationNo, incomeNo, accountNo, expenseNo, friendId) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setString(3, phone);
            stmt.setDouble(4, hotcash);
            stmt.setDouble(5, budget);
            stmt.setInt(6, notificationNo);
            stmt.setInt(7, incomeNo);
            stmt.setInt(8, accountNo);
            stmt.setInt(9, expenseNo);
            stmt.setString(10, friendId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Full user registration successful.");
            } else {
                System.out.println("❌ Full user registration failed.");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("⚠️ Email already registered.");
            } else {
                e.printStackTrace();
            }
        }
    }
    
    public void updateUser(String email, String name, String phone, double hotcash, double budget) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "UPDATE User_Profile SET name = ?, phoneNumber = ?, hotcash = ?, budget = ? WHERE emailId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setDouble(3, hotcash);
            ps.setDouble(4, budget);
            ps.setString(5, email);
            ps.executeUpdate();
            System.out.println("✅ User updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String email) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM User_Profile WHERE emailId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.executeUpdate();
            System.out.println("🗑️ User deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 // Add to UserService.java
    public void showUserProfile(String emailId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM User_Profile WHERE emailId = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, emailId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- User Profile ---");
                System.out.println("Email: " + rs.getString("emailId"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Phone: " + rs.getString("phoneNumber"));
                System.out.println("Hotcash: " + rs.getDouble("hotcash"));
                System.out.println("Budget: " + rs.getDouble("budget"));
                System.out.println("Friend ID: " + rs.getString("friendId"));
            } else {
                System.out.println("❌ No profile found for this user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
