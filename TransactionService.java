package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class TransactionService {

    public void addIncome(int incomeNo, double amount, String description, String email) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Income (incomeNo, amount, description, emailId) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, incomeNo);
            ps.setDouble(2, amount);
            ps.setString(3, description);
            ps.setString(4, email);
            ps.executeUpdate();
            System.out.println("Income added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addExpense(int expenseNo, double amount, String category, String location, String date, int accountNo) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Expense (expenseNo, amount, category, location, date, startDate, endDate, image, accountNo) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, expenseNo);
            ps.setDouble(2, amount);
            ps.setString(3, category);
            ps.setString(4, location);
            ps.setString(5, date);      // 'YYYY-MM-DD'
            ps.setString(6, date);      // startDate
            ps.setString(7, date);      // endDate
            ps.setString(8, "img.jpg"); // Placeholder image
            ps.setInt(9, accountNo);
            ps.executeUpdate();
            System.out.println("Expense added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showTotalIncome(String email) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT SUM(amount) AS totalIncome FROM Income WHERE emailId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Total Income: ₹" + rs.getDouble("totalIncome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showTotalExpenses(int accountNo) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT SUM(amount) AS totalExpense FROM Expense WHERE accountNo = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, accountNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Total Expense: ₹" + rs.getDouble("totalExpense"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateIncome(int incomeNo, double amount, String description) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "UPDATE Income SET amount = ?, description = ? WHERE incomeNo = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, amount);
            ps.setString(2, description);
            ps.setInt(3, incomeNo);
            ps.executeUpdate();
            System.out.println("✅ Income updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteIncome(int incomeNo) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM Income WHERE incomeNo = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, incomeNo);
            ps.executeUpdate();
            System.out.println("🗑️ Income deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateEMI(int emiNo, double emiAmount, String emiPay, String dueDate, String startDate,
            String emiDescription, double balanceAmount, int loanNo) {
    	try (Connection con = DatabaseConnection.getConnection()) {
    		String query = "UPDATE EMI SET emiAmount = ?, emiPay = ?, dueDate = ?, startDate = ?, emiDescription = ?, balanceAmount = ?, loanNo = ? WHERE emiNo = ?";
    		PreparedStatement ps = con.prepareStatement(query);
    		ps.setDouble(1, emiAmount);
    		ps.setString(2, emiPay);
    		ps.setString(3, dueDate);
    		ps.setString(4, startDate);
    		ps.setString(5, emiDescription);
    		ps.setDouble(6, balanceAmount);
    		ps.setInt(7, loanNo);
    		ps.setInt(8, emiNo);
    		ps.executeUpdate();
    		System.out.println("✅ EMI updated successfully.");
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

	public void deleteEMI(int emiNo) {
		try (Connection con = DatabaseConnection.getConnection()) {
			String query = "DELETE FROM EMI WHERE emiNo = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, emiNo);
			ps.executeUpdate();
			System.out.println("🗑️ EMI deleted successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	 public void showEMIsForUser(String email) {
	        try (Connection con = DatabaseConnection.getConnection()) {
	            String query = "SELECT E.emiNo, E.emiAmount, E.dueDate, E.emiPay, L.loanNo " +
	                           "FROM EMI E JOIN Loan L ON E.loanNo = L.loanNo " +
	                           "JOIN Account A ON L.accountNo = A.accountNo " +
	                           "WHERE A.emailId = ?";
	            PreparedStatement ps = con.prepareStatement(query);
	            ps.setString(1, email);
	            ResultSet rs = ps.executeQuery();

	            System.out.println("\n--- EMI Details ---");
	            while (rs.next()) {
	                System.out.println("EMI No       : " + rs.getInt("emiNo"));
	                System.out.println("Loan No      : " + rs.getInt("loanNo"));
	                System.out.println("Amount       : ₹" + rs.getDouble("emiAmount"));
	                System.out.println("Due Date     : " + rs.getDate("dueDate"));
	                System.out.println("Payment Status: " + rs.getString("emiPay"));
	                System.out.println("-----------------------------");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	 }
	 
	 public void deleteExpense(int expenseNo) {
		    try (Connection con = DatabaseConnection.getConnection()) {
		        String query = "DELETE FROM Expense WHERE expenseNo = ?";
		        PreparedStatement ps = con.prepareStatement(query);
		        ps.setInt(1, expenseNo);
		        ps.executeUpdate();
		        System.out.println("🗑️ Expense deleted successfully.");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
	}
	 
	 public void updateExpense(int expenseNo, double amount, String category, String location, String date, int accountNo) {
		    try (Connection con = DatabaseConnection.getConnection()) {
		        String query = "UPDATE Expense SET amount = ?, category = ?, location = ?, date = ?, startDate = ?, endDate = ?, image = ?, accountNo = ? WHERE expenseNo = ?";
		        PreparedStatement ps = con.prepareStatement(query);
		        ps.setDouble(1, amount);
		        ps.setString(2, category);
		        ps.setString(3, location);
		        ps.setString(4, date);  // date
		        ps.setString(5, date);  // startDate
		        ps.setString(6, date);  // endDate
		        ps.setString(7, "img_updated.jpg"); // placeholder image update
		        ps.setInt(8, accountNo);
		        ps.setInt(9, expenseNo);
		        ps.executeUpdate();
		        System.out.println("✅ Expense updated successfully.");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
	}



}
