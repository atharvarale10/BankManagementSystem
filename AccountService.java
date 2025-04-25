package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountService {

    public void showUserAccounts(String email) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT accountNo, accountType, balance FROM Account WHERE emailId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Account Details ---");
            while (rs.next()) {
                System.out.println("Account No: " + rs.getInt("accountNo"));
                System.out.println("Type      : " + rs.getString("accountType"));
                System.out.println("Balance   : " + rs.getDouble("balance"));
                System.out.println("-----------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void createAccount(int accountNo, String accountType, double balance, String email) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Account (accountNo, accountType, balance, emailId) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, accountNo);
            ps.setString(2, accountType);
            ps.setDouble(3, balance);
            ps.setString(4, email);
            ps.executeUpdate();
            System.out.println("✅ Account created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAccountBalance(int accountNo, double balance) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "UPDATE Account SET balance = ? WHERE accountNo = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, balance);
            ps.setInt(2, accountNo);
            ps.executeUpdate();
            System.out.println("✅ Account updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccountByEmail(String emailId) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM Account WHERE emailId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, emailId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("🗑️ Account(s) deleted for " + emailId);
            } else {
                System.out.println("⚠️ No account found for the given email.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
