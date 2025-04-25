package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanService {

    public void showLoanDetails(String email) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT L.loanNo, L.loanAmount, L.interest, L.balanceAmount, L.loanType, A.accountNo " +
                           "FROM Loan L JOIN Account A ON L.accountNo = A.accountNo " +
                           "WHERE A.emailId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Loan Details ---");
            while (rs.next()) {
                System.out.println("Loan No       : " + rs.getInt("loanNo"));
                System.out.println("Loan Type     : " + rs.getString("loanType"));
                System.out.println("Loan Amount   : ₹" + rs.getDouble("loanAmount"));
                System.out.println("Interest Rate : " + rs.getDouble("interest") + "%");
                System.out.println("Balance Left  : ₹" + rs.getDouble("balanceAmount"));
                System.out.println("Account No    : " + rs.getInt("accountNo"));
                System.out.println("-----------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateLoan(int loanNo, double loanAmount, double interest, double balanceAmount,
            String startDate, String endDate, String loanType, int accountNo) {
			try (Connection con = DatabaseConnection.getConnection()) {
				String query = "UPDATE Loan SET loanAmount = ?, interest = ?, balanceAmount = ?, startDate = ?, endDate = ?, loanType = ?, accountNo = ? WHERE loanNo = ?";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setDouble(1, loanAmount);
				ps.setDouble(2, interest);
				ps.setDouble(3, balanceAmount);
				ps.setString(4, startDate);
				ps.setString(5, endDate);
				ps.setString(6, loanType);
				ps.setInt(7, accountNo);
				ps.setInt(8, loanNo);
				ps.executeUpdate();
				System.out.println("✅ Loan updated successfully.");
			} catch (SQLException e) {
			    e.printStackTrace();
			}
	}

	public void deleteLoan(int loanNo) {
		try (Connection con = DatabaseConnection.getConnection()) {
			String query = "DELETE FROM Loan WHERE loanNo = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, loanNo);
			ps.executeUpdate();
			System.out.println("🗑️ Loan deleted successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
